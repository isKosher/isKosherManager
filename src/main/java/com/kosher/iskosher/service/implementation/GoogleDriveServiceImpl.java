package com.kosher.iskosher.service.implementation;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.kosher.iskosher.common.enums.FolderType;
import com.kosher.iskosher.common.utils.FileValidatorUtil;
import com.kosher.iskosher.dto.response.FileUploadResponse;
import com.kosher.iskosher.exception.FileOperationException;
import com.kosher.iskosher.service.FileStorageService;
import com.kosher.iskosher.types.FileType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service("googleDriveService")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.google-drive.enabled", havingValue = "true")
public class GoogleDriveServiceImpl implements FileStorageService {
    private final Drive googleDriveClient;
    private final FileValidatorUtil fileValidator;

    @Value("${storage.google-drive.folders.certificates-id}")
    private String certificatesFolderId;

    @Value("${storage.google-drive.folders.supervisors-id}")
    private String supervisorsFolderId;
    private Map<FolderType, String> folderIds;

    @PostConstruct
    public void init() {
        folderIds = new EnumMap<>(FolderType.class);
        folderIds.put(FolderType.CERTIFICATES, certificatesFolderId);
        folderIds.put(FolderType.SUPERVISORS, supervisorsFolderId);

        log.info("Initialized GoogleDriveService");
        validateFolderIds();
    }

    @Override
    public FileUploadResponse uploadFile(MultipartFile file, FolderType folderType) {
        fileValidator.validateFile(file);
        String folderId = getFolderIdByType(folderType);

        try {
            String fileName = generateUniqueFileName(file.getOriginalFilename());

            File fileMetadata = new File()
                    .setName(fileName)
                    .setParents(Collections.singletonList(folderId));

            InputStreamContent mediaContent = new InputStreamContent(
                    file.getContentType(),
                    file.getInputStream()
            );

            File uploadedFile = googleDriveClient.files().create(fileMetadata, mediaContent)
                    .setFields("id, webViewLink, mimeType, size")
                    .execute();

            log.info("Successfully uploaded file: {} with ID: {} to folder: {}",
                    fileName, uploadedFile.getId(), folderType);

            String url = isImageFile(uploadedFile.getMimeType())
                    ? getDirectImageUrl(uploadedFile.getId())
                    : uploadedFile.getWebViewLink();

            return new FileUploadResponse(
                    uploadedFile.getId(),
                    url,
                    fileName,
                    uploadedFile.getMimeType(),
                    uploadedFile.getSize()
            );

        } catch (IOException e) {
            log.error("Failed to upload file: {} to folder: {}", file.getOriginalFilename(), folderType, e);
            throw new FileOperationException("Failed to upload file to Google Drive: " + e.getMessage());
        }
    }

    @Override
    public FileType getFile(String fileId) {
        try {
            File file = googleDriveClient.files().get(fileId)
                    .setFields("id, name, webViewLink, mimeType, parents, size")
                    .execute();

            String parentFolderId = file.getParents().get(0);
            FolderType folderType = getFolderTypeById(parentFolderId);

            String url = isImageFile(file.getMimeType())
                    ? getDirectImageUrl(file.getId())
                    : file.getWebViewLink();

            return new FileType(
                    file.getId(),
                    file.getName(),
                    file.getWebViewLink(),
                    file.getMimeType(),
                    folderType
            );

        } catch (IOException e) {
            log.error("Failed to get file with ID: {}", fileId, e);
            throw new FileOperationException("Failed to get file from Google Drive: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(FolderType folderType, String fileId) {
        try {
            googleDriveClient.files().delete(fileId).execute();
            log.info("Successfully deleted file with ID: {} from folder: {}", fileId, folderType);
        } catch (IOException e) {
            log.error("Failed to delete file with ID: {} from folder: {}", fileId, folderType, e);
            throw new FileOperationException("Failed to delete file from Google Drive: " + e.getMessage());
        }
    }

    @Override
    public boolean isAvailable() {
        try {
            // Simple health check
            googleDriveClient.about().get().setFields("user").execute();
            return true;
        } catch (Exception e) {
            log.warn("Google Drive service is not available: {}", e.getMessage());
            return false;
        }
    }

    private String getDirectImageUrl(String fileId) {
        return "https://drive.google.com/uc?export=view&id=" + fileId;
    }

    private String generateUniqueFileName(String originalFilename) {
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String baseName = StringUtils.stripFilenameExtension(originalFilename);
        return String.format("%s_%s.%s",
                baseName,
                UUID.randomUUID().toString().substring(0, 8),
                extension
        );
    }

    private void validateFolderIds() {
        for (Map.Entry<FolderType, String> entry : folderIds.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                throw new FileOperationException(
                        "Google Drive folder ID not configured for " + entry.getKey()
                );
            }
        }
    }

    private String getFolderIdByType(FolderType folderType) {
        String folderId = folderIds.get(folderType);
        if (folderId == null || folderId.isEmpty()) {
            throw new FileOperationException("Folder ID not configured for type: " + folderType);
        }
        return folderId;
    }

    private FolderType getFolderTypeById(String folderId) {
        return folderIds.entrySet().stream()
                .filter(entry -> entry.getValue().equals(folderId))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private boolean isImageFile(String mimeType) {
        return mimeType != null && mimeType.startsWith("image/");
    }
}
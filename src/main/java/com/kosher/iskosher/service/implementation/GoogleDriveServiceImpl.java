package com.kosher.iskosher.service.implementation;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.kosher.iskosher.common.enums.FolderGoogleType;
import com.kosher.iskosher.dto.response.FileUploadResponse;
import com.kosher.iskosher.exception.FileOperationException;
import com.kosher.iskosher.exception.InvalidFileException;
import com.kosher.iskosher.service.GoogleDriveService;
import com.kosher.iskosher.types.FileType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleDriveServiceImpl implements GoogleDriveService {
    private final Drive googleDriveClient;

    @Value("${google.drive.folders.certificates-id}")
    private String certificatesFolderId;

    @Value("${google.drive.folders.supervisors-id}")
    private String supervisorsFolderId;

    @Value("${google.drive.allowed-extensions:pdf,jpg,jpeg,png}")
    private String allowedExtensions;

    @Value("${google.drive.max-file-size:10485760}")
    private long maxFileSize;

    private Set<String> allowedExtensionsSet;
    private Map<FolderGoogleType, String> folderIds;


    @PostConstruct
    public void init() {
        allowedExtensionsSet = new HashSet<>(Arrays.asList(allowedExtensions.split(",")));

        folderIds = new EnumMap<>(FolderGoogleType.class);
        folderIds.put(FolderGoogleType.CERTIFICATES, certificatesFolderId);
        folderIds.put(FolderGoogleType.SUPERVISORS, supervisorsFolderId);

        log.info("Initialized GoogleDriveService with allowed extensions: {}", allowedExtensionsSet);
        validateFolderIds();
    }

    /**
     * Uploads a file to a specific Google Drive folder
     *
     * @param file       The file to upload
     * @param folderType The type of folder (Certificates or Supervisors)
     * @return A response containing the file ID and its link
     */
    public FileUploadResponse uploadFile(MultipartFile file, FolderGoogleType folderType) {
        validateFile(file);
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
                    .setFields("id, webViewLink")
                    .execute();

            log.info("Successfully uploaded file: {} with ID: {} to folder: {}",
                    fileName, uploadedFile.getId(), folderType);

            return new FileUploadResponse(uploadedFile.getId(), uploadedFile.getWebViewLink());

        } catch (IOException e) {
            log.error("Failed to upload file: {} to folder: {}", file.getOriginalFilename(), folderType, e);
            throw new FileOperationException("Failed to upload file: " + e.getMessage());
        }
    }

    /**
     * Retrieves metadata for a file from Google Drive.
     *
     * @param fileId The unique ID of the file.
     * @return A {@link FileType} containing the file's details.
     * @throws FileOperationException If retrieval fails.
     */
    public FileType getFile(String fileId) {
        try {
            File file = googleDriveClient.files().get(fileId)
                    .setFields("id, name, webViewLink, mimeType, parents")
                    .execute();

            String parentFolderId = file.getParents().get(0);
            FolderGoogleType folderType = getFolderTypeById(parentFolderId);

            return new FileType(
                    file.getId(),
                    file.getName(),
                    file.getWebViewLink(),
                    file.getMimeType(),
                    folderType
            );

        } catch (IOException e) {
            log.error("Failed to get file with ID: {}", fileId, e);
            throw new FileOperationException("Failed to get file: " + e.getMessage());
        }
    }

    /**
     * Permanently deletes a file from Google Drive.
     *
     * @param fileId The unique ID of the file to delete.
     * @throws FileOperationException If the deletion fails.
     */
    public void deleteFile(String fileId) {
        try {
            googleDriveClient.files().delete(fileId).execute();
            log.info("Successfully deleted file with ID: {}", fileId);
        } catch (IOException e) {
            log.error("Failed to delete file with ID: {}", fileId, e);
            throw new FileOperationException("Failed to delete file: " + e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("File is empty");
        }

        if (file.getSize() > maxFileSize) {
            throw new InvalidFileException(
                    String.format("File size exceeds maximum limit of %d bytes", maxFileSize)
            );
        }

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (extension == null || !allowedExtensionsSet.contains(extension.toLowerCase())) {
            throw new InvalidFileException(String.format("File type %s is not allowed. Allowed types: %s"
                    , extension
                    , allowedExtensionsSet)
            );
        }
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
        for (Map.Entry<FolderGoogleType, String> entry : folderIds.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                throw new FileOperationException(
                        "Folder ID not configured for " + entry.getKey()
                );
            }
        }

    }

    private String getFolderIdByType(FolderGoogleType folderType) {
        String folderId = folderIds.get(folderType);
        if (folderId == null || folderId.isEmpty()) {
            throw new FileOperationException("Folder ID not configured for type: " + folderType);
        }
        return folderId;
    }

    private FolderGoogleType getFolderTypeById(String folderId) {
        return folderIds.entrySet().stream()
                .filter(entry -> entry.getValue().equals(folderId))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

}
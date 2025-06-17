package com.kosher.iskosher.service.implementation;

import com.kosher.iskosher.common.enums.FolderType;
import com.kosher.iskosher.common.utils.FileValidatorUtil;
import com.kosher.iskosher.dto.response.FileUploadResponse;
import com.kosher.iskosher.exception.FileOperationException;
import com.kosher.iskosher.service.FileStorageService;
import com.kosher.iskosher.types.FileType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service("supabaseService")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.supabase.enabled", havingValue = "true")
public class SupabaseServiceImpl implements FileStorageService {
    private final RestTemplate restTemplate;
    private final FileValidatorUtil fileValidator;

    @Value("${storage.supabase.url}")
    private String supabaseUrl;

    @Value("${storage.supabase.key}")
    private String supabaseKey;

    @Value("${storage.supabase.bucket}")
    private String bucketName;

    @Override
    public FileUploadResponse uploadFile(MultipartFile file, FolderType folderType) {
        fileValidator.validateFile(file);

        try {
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            String uploadUrl = String.format("%s/storage/v1/object/%s/%s/%s",
                    supabaseUrl, bucketName, folderType.getPath(), fileName);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.set("Content-Type", file.getContentType());

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    uploadUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                String publicUrl = getPublicUrl(fileName, folderType);

                log.info("Successfully uploaded file: {} to Supabase folder: {}",
                        fileName, folderType.getPath());

                return new FileUploadResponse(
                        fileName,
                        publicUrl,
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getSize()
                );
            } else {
                throw new FileOperationException("Failed to upload file to Supabase");
            }

        } catch (Exception e) {
            log.error("Failed to upload file: {} to Supabase folder: {}",
                    file.getOriginalFilename(), folderType, e);
            throw new FileOperationException("Failed to upload file to Supabase: " + e.getMessage());
        }
    }

    @Override
    public FileType getFile(String fileId) {
        // TODO: Implementation for getting file metadata from Supabase
        throw new UnsupportedOperationException("Get file not yet implemented for Supabase");
    }

    @Override
    public void deleteFile(FolderType folderType, String fileId) {
        try {
            String deleteUrl = String.format("%s/storage/v1/object/%s/%s/%s",
                    supabaseUrl, bucketName, folderType.getPath(), fileId);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Void> response = restTemplate.exchange(
                    deleteUrl,
                    HttpMethod.DELETE,
                    requestEntity,
                    Void.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully deleted file: {}/{} from Supabase", folderType.getPath(), fileId);
            } else {
                throw new FileOperationException("Failed to delete file from Supabase");
            }

        } catch (Exception e) {
            log.error("Failed to delete file: {}/{} from Supabase", folderType.getPath(), fileId, e);
            throw new FileOperationException("Failed to delete file from Supabase: " + e.getMessage());
        }
    }


    @Override
    public boolean isAvailable() {
        try {
            String healthUrl = String.format("%s/rest/v1/", supabaseUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", supabaseKey);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    healthUrl,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.warn("Supabase service is not available: {}", e.getMessage());
            return false;
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

    private String getPublicUrl(String fileName, FolderType folderType) {
        return String.format("%s/storage/v1/object/public/%s/%s/%s",
                supabaseUrl, bucketName, folderType.getPath(), fileName);
    }
}
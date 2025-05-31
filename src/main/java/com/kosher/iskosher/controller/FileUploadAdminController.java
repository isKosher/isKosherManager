package com.kosher.iskosher.controller;

import com.kosher.iskosher.common.enums.FolderType;
import com.kosher.iskosher.common.enums.StorageProvider;
import com.kosher.iskosher.dto.response.FileUploadResponse;
import com.kosher.iskosher.service.FileStorageService;
import com.kosher.iskosher.service.factory.FileStorageFactory;
import com.kosher.iskosher.types.FileType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/files")
@RequiredArgsConstructor
public class FileUploadAdminController {
    private final FileStorageFactory fileStorageFactory;

    @PostMapping("/upload/{provider}/{folderType}")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable StorageProvider provider,
            @PathVariable FolderType folderType) {

        log.info("Received file upload request for: {} to folder: {} using provider: {}",
                file.getOriginalFilename(), folderType, provider);

        FileStorageService storageService = fileStorageFactory.getStorageService(provider);
        FileUploadResponse response = storageService.uploadFile(file, folderType);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{provider}/{fileId}")
    public ResponseEntity<FileType> getFile(
            @PathVariable StorageProvider provider,
            @PathVariable String fileId) {

        FileStorageService storageService = fileStorageFactory.getStorageService(provider);
        FileType fileType = storageService.getFile(fileId);

        return ResponseEntity.ok(fileType);
    }

    @DeleteMapping("/{provider}/{folderType}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable StorageProvider provider,
            @PathVariable FolderType folderType,
            @RequestParam("fileId") String fileId) {

        FileStorageService storageService = fileStorageFactory.getStorageService(provider);
        storageService.deleteFile(folderType, fileId);

        return ResponseEntity.ok().build();
    }

}

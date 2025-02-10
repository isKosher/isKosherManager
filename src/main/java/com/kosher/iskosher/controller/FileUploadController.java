package com.kosher.iskosher.controller;

import com.kosher.iskosher.common.enums.FolderGoogleType;
import com.kosher.iskosher.dto.response.FileUploadResponse;
import com.kosher.iskosher.service.GoogleDriveService;
import com.kosher.iskosher.types.FileType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileUploadController {
    private final GoogleDriveService googleDriveService;


    @PostMapping("/upload/{folderType}")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable FolderGoogleType folderType) {
        log.info("Received file upload request for: {} to folder: {}",
                file.getOriginalFilename(), folderType);
        return ResponseEntity.ok(googleDriveService.uploadFile(file, folderType));
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<FileType> getFile(@PathVariable String fileId) {
        return ResponseEntity.ok(googleDriveService.getFile(fileId));
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileId) {
        googleDriveService.deleteFile(fileId);
        return ResponseEntity.ok().build();
    }
}
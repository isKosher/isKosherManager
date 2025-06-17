package com.kosher.iskosher.service;

import com.kosher.iskosher.common.enums.FolderType;
import com.kosher.iskosher.dto.response.FileUploadResponse;
import com.kosher.iskosher.types.FileType;
import org.springframework.web.multipart.MultipartFile;


public interface FileStorageService {
    FileUploadResponse uploadFile(MultipartFile file, FolderType folderType);
    FileType getFile(String fileId);
    void deleteFile(FolderType folderType, String fileId);
    boolean isAvailable();
}
package com.kosher.iskosher.service;

import com.kosher.iskosher.common.enums.FolderGoogleType;
import com.kosher.iskosher.dto.response.FileUploadResponse;
import com.kosher.iskosher.types.FileType;
import org.springframework.web.multipart.MultipartFile;


public interface GoogleDriveService {
    FileUploadResponse uploadFile(MultipartFile file, FolderGoogleType folderType);

    FileType getFile(String fileId);

    void deleteFile(String fileId);
}
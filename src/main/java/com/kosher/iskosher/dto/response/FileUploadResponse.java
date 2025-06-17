package com.kosher.iskosher.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResponse {
    private String id;
    private String webViewLink;
    private String fileName;
    private String fileType;
    private Long fileSize;
}
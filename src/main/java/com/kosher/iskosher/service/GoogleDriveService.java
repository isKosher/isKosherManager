package com.kosher.iskosher.service;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoogleDriveService {
    private final Drive googleDriveClient;
    private final String GOOGLE_DRIVE_FOLDER_ID = "1-4vCnqfT_j0d3PBI_2B8jAS219UtbUcQ"; // Replace with your folder ID

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        // Create metadata for the file
        File fileMetadata = new File();
        fileMetadata.setName(multipartFile.getOriginalFilename());
        fileMetadata.setParents(Collections.singletonList(GOOGLE_DRIVE_FOLDER_ID));

        // Create media content from the InputStream
        InputStreamContent mediaContent = new InputStreamContent(
                multipartFile.getContentType(),
                multipartFile.getInputStream()
        );

        // Upload the file to Google Drive
        File uploadedFile = googleDriveClient.files().create(fileMetadata, mediaContent)
                .setFields("id, webViewLink")
                .execute();

        return uploadedFile.getWebViewLink(); // Return the public URL of the uploaded file
    }
    public List<String> listFiles() throws IOException {
        return googleDriveClient.files().list()
                .setQ("'" + GOOGLE_DRIVE_FOLDER_ID + "' in parents")
                .setFields("files(id, name, webViewLink)")
                .execute()
                .getFiles()
                .stream()
                .map(file -> "Name: " + file.getName() + " | URL: " + file.getWebViewLink())
                .collect(Collectors.toList());
    }

    public void deleteFile(String fileId) throws IOException {
        googleDriveClient.files().delete(fileId).execute();
    }
}


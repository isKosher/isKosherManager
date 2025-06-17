package com.kosher.iskosher.common.utils;

import com.kosher.iskosher.exception.InvalidFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class FileValidatorUtil {

    @Value("${storage.allowed-extensions}")
    private String allowedExtensions;

    @Value("${storage.max-file-size}")
    private long maxFileSize;

    private Set<String> allowedExtensionsSet;

    public void validateFile(MultipartFile file) {
        if (allowedExtensionsSet == null) {
            allowedExtensionsSet = new HashSet<>(Arrays.asList(allowedExtensions.split(",")));
        }

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
            throw new InvalidFileException(String.format("File type %s is not allowed. Allowed types: %s",
                    extension, allowedExtensionsSet));
        }
    }
}
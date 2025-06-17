package com.kosher.iskosher.types;

import com.kosher.iskosher.common.enums.FolderType;

import java.io.Serializable;


public record FileType(
        String id,
        String name,
        String webViewLink,
        String mimeType,
        FolderType folderType
) implements Serializable {
}
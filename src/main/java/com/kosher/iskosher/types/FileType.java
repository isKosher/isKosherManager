package com.kosher.iskosher.types;

import com.kosher.iskosher.common.enums.FolderGoogleType;

import java.io.Serializable;


public record FileType(
        String id,
        String name,
        String webViewLink,
        String mimeType,
        FolderGoogleType folderType
) implements Serializable {
}
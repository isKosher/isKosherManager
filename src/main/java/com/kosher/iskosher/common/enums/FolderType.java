package com.kosher.iskosher.common.enums;

public enum FolderType {
    CERTIFICATES("certificates"),
    SUPERVISORS("supervisors"),
    BUSINESS_PHOTOS("business_photos");

    private final String path;

    FolderType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

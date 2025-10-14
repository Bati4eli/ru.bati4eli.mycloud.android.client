package ru.bati4eli.smartcloud.android.client.utils;

import java.io.File;

public enum DefaultFolders {
    PREVIEWS,
    ORIGIN,
    FACES;
    private File folder;

    public void setUpDirectory(File folder) {
        this.folder = folder;
    }

    public File getFolder() {
        return folder;
    }
}

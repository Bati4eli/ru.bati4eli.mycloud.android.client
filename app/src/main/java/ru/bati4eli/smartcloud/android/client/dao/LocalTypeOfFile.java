package ru.bati4eli.smartcloud.android.client.dao;

public enum LocalTypeOfFile {
    FILE(0),
    DOCUMENT(1),
    IMAGE(2),
    VIDEO(3),
    TEXT(4),
    ROOT(100),
    TRASHBIN(101),
    FOLDER(102),
    SHARED_FOLDER(103);

    private final int value;

    LocalTypeOfFile(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static LocalTypeOfFile fromValue(int value) {
        for (LocalTypeOfFile type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}

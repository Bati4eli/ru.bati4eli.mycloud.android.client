package ru.bati4eli.smartcloud.android.client.enums;

import lombok.Getter;

public enum SortByEnum {
    SORT_BY_CHANGE_DATE(10),
    SORT_BY_CREATE_DATE(15),
    SORT_BY_NAME(20),
    SORT_BY_TYPE(30),
    SORT_BY_SIZE(40);

    @Getter
    private int parameterId;

    SortByEnum(int i) {
        this.parameterId = i;
    }

    public static SortByEnum of(int parameterId) {
        for (SortByEnum sortBy : SortByEnum.values()) {
            if (sortBy.parameterId == parameterId) {
                return sortBy;
            }
        }
        return null;
    }
}

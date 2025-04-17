package ru.bati4eli.smartcloud.android.client.enums;

import lombok.Getter;

public enum SortOrderEnum {
    SORT_ASCENDING(100),
    SORT_DESCENDING(200);

    @Getter
    private int parameterId;

    SortOrderEnum(int i) {
        this.parameterId = i;
    }

    public static SortOrderEnum of(int parameterId) {
        for (SortOrderEnum sortOrder : SortOrderEnum.values()) {
            if (sortOrder.parameterId == parameterId) {
                return sortOrder;
            }
        }
        return null;
    }
}

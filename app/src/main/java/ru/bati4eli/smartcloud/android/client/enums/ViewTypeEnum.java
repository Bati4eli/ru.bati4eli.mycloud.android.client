package ru.bati4eli.smartcloud.android.client.enums;

import lombok.Getter;

public enum ViewTypeEnum {
    VIEW_LIST(1),
    VIEW_GRID(2);

    @Getter
    private int parameterId;

    ViewTypeEnum(int i) {
        this.parameterId = i;
    }

    public static ViewTypeEnum of(int parameterId) {
        for (ViewTypeEnum v : ViewTypeEnum.values()) {
            if (v.parameterId == parameterId) {
                return v;
            }
        }
        return null;
    }
}

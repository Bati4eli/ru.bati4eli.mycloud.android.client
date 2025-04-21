package ru.bati4eli.smartcloud.android.client.tabs.helpers;

import ru.bati4eli.smartcloud.android.client.enums.GroupNameEnum;

public interface OnChangedSortOrView {
    void onParametersChanged(GroupNameEnum groupName, Integer value);
}

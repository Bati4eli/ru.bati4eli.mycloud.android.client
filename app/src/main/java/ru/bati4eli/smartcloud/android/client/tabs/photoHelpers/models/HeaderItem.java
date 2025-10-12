package ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.time.YearMonth;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class HeaderItem implements Item {
    private final YearMonth yearMonth;

}

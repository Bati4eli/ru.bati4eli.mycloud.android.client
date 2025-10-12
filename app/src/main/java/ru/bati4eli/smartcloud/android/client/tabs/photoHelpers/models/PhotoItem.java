package ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.bati4eli.mycloud.repo.ShortMediaInfoDto;

import java.time.YearMonth;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class PhotoItem implements Item {
    final YearMonth yearMonth;
    final ShortMediaInfoDto photo;
    boolean loaded = false;

}

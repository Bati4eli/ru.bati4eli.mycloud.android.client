package ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models;

import android.annotation.SuppressLint;
import lombok.Data;
import ru.bati4eli.mycloud.repo.DateCounterResponse;

import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;


@Data
@SuppressLint("NewApi")
public class MonthBucket {
    private final YearMonth yearMonth;
    private final long amount;
    private final boolean loaded = false;
    private final int startIndexPhoto = 0;
    private final int endIndexPhoto = 0;

    public MonthBucket(DateCounterResponse response) {
        this.yearMonth = YearMonth.of(response.getYear(), response.getMonth());
        this.amount = response.getAmount();
    }

    public OffsetDateTime getStartFilter() {
        return yearMonth.atDay(1).atStartOfDay().atOffset(ZoneOffset.UTC);
    }

    public OffsetDateTime getEndFilter() {
        return yearMonth.plusMonths(1)
                .atDay(1)
                .minus(1, ChronoUnit.MILLIS)
                .atStartOfDay()
                .atOffset(ZoneOffset.UTC);
    }
}

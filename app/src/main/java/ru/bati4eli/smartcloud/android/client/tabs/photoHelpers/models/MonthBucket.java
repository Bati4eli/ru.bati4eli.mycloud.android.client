package ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models;

import android.annotation.SuppressLint;
import lombok.Data;
import ru.bati4eli.mycloud.repo.DateCounterResponse;

import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;


@Data
@SuppressLint("NewApi")
public class MonthBucket {
    private final YearMonth yearMonth;
    private final long amount;
    private boolean loaded = false;
    private int startIndexPhoto = 0;
    private AtomicInteger index = new AtomicInteger(0);
    private int endIndexPhoto = 0;

    public MonthBucket(DateCounterResponse response) {
        this.yearMonth = YearMonth.of(response.getYear(), response.getMonth());
        this.amount = response.getAmount();
    }

    public void setStartIndexPhoto(int startIndexPhoto) {
        this.startIndexPhoto = startIndexPhoto;
        this.index.set(startIndexPhoto);
    }

    public OffsetDateTime getStartFilter() {
        return yearMonth.atDay(1).atStartOfDay().atOffset(ZoneOffset.UTC);
    }

    public OffsetDateTime getEndFilter() {
        return yearMonth.plusMonths(1)
                .atDay(1)
                .atStartOfDay()
                .atOffset(ZoneOffset.UTC);
    }
}

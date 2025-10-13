package ru.bati4eli.smartcloud.android.client.tabs.photoHelpers;

import lombok.RequiredArgsConstructor;
import ru.bati4eli.mycloud.repo.ShortMediaInfoDto;
import ru.bati4eli.smartcloud.android.client.service.observers.BaseStreamObserver;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.MonthBucket;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class PhotoObserver extends BaseStreamObserver<ShortMediaInfoDto> {

    private final MonthBucket bucket;
    private final BiConsumer<MonthBucket, ShortMediaInfoDto> onNext;
    private final Runnable onCompleted;
    private final Consumer<Throwable> onError;

    @Override
    public void onNext(ShortMediaInfoDto item) {
        onNext.accept(bucket, item);
    }

    @Override
    public void onCompleted() {
        onCompleted.run();
    }

    @Override
    public void onError(Throwable throwable) {
        super.onError(throwable);
        onError.accept(throwable);
    }
}

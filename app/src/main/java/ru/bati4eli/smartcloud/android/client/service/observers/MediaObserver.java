package ru.bati4eli.smartcloud.android.client.service.observers;

import ru.bati4eli.mycloud.repo.ShortMediaInfoDto;

public class MediaObserver extends SyncStreamObserver<ShortMediaInfoDto> {
    @Override
    public void onNext(ShortMediaInfoDto shortMediaInfoDto) {
        // Реализация метода onNext
    }

    @Override
    public void onError(Throwable throwable) {
        // Реализация метода onError
    }

    @Override
    public void onCompleted() {
        // Реализация метода onCompleted
    }
}

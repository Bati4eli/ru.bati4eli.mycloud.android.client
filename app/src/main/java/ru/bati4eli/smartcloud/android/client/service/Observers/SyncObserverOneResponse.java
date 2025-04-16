package ru.bati4eli.smartcloud.android.client.service.Observers;

import io.grpc.stub.StreamObserver;

import java.util.concurrent.atomic.AtomicBoolean;

public class SyncObserverOneResponse<TYPE> implements StreamObserver<TYPE> {

    private TYPE response;
    private Throwable error;
    private AtomicBoolean working = new AtomicBoolean(true);

    @Override
    public void onNext(TYPE response) {
        this.response = response;
    }

    @Override
    public void onError(Throwable throwable) {
        working.set(false);
        this.error = throwable;
    }

    @Override
    public void onCompleted() {
        working.set(false);
    }

    public TYPE getResponse() {
        while (working.get()) {
        }
        if (error != null) {
            throw new RuntimeException(error.getMessage(), error);
        }
        return response;
    }
}

package ru.bati4eli.smartcloud.android.client.service.observers;

import io.grpc.stub.StreamObserver;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseStreamObserver<TYPE> implements StreamObserver<TYPE>  {
    protected TYPE response;
    protected Throwable error;
    protected AtomicBoolean working = new AtomicBoolean(true);

    public final boolean isWorking() {
        return working.get();
    }

    public final void waiting() {
        while (isWorking()) {
        }
    }

    @Override
    public void onError(Throwable throwable) {
        working.set(false);
        error = throwable;
    }
}

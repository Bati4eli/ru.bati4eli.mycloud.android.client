package ru.bati4eli.smartcloud.android.client.service.observers;

public class SyncObserverOneResponse<TYPE> extends BaseStreamObserver<TYPE> {

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

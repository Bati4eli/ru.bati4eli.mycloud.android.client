package ru.bati4eli.smartcloud.android.client.service.observers;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StreamObserverIterator<TYPE> extends BaseStreamObserver<TYPE> implements Iterator<TYPE> {
    private final ConcurrentLinkedQueue<TYPE> queue = new ConcurrentLinkedQueue<>();
    private final Object lock = new Object();

    @Override
    public void onNext(TYPE value) {
        queue.offer(value);
    }

    @Override
    public void onError(Throwable throwable) {
        super.onError(throwable);
        synchronized (lock) {
            lock.notifyAll(); // Разбудить потенциальных ожидающих итераторов
        }
    }

    @Override
    public void onCompleted() {
        working.set(false);
        synchronized (lock) {
            lock.notifyAll(); // Разбудить потенциальных ожидающих итераторов
        }
    }

    @Override
    public boolean hasNext() {
        // Если мы еще работаем и очередь пуста, мы ждем, пока не будут получены новые элементы или завершится поток.
        synchronized (lock) {
            while (isWorking() && queue.isEmpty()) {
                try {
                    lock.wait(); // Ждем, пока не будет вызван notify
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Восстанавливаем статус прерывания
                    return false;
                }
            }
        }

        // Если очередь не пуста, возвращаем true
        return !queue.isEmpty();
    }

    @Override
    public TYPE next() {
        if (!hasNext()) {
            throw new IllegalStateException("No more elements available");
        }
        return queue.poll(); // Извлекаем элемент из очереди
    }

    public Throwable getError() {
        return error;
    }
}

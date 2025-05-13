package ru.bati4eli.smartcloud.android.client.service.observers;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class StreamObserverIterator<TYPE> extends BaseStreamObserver<TYPE> implements Iterator<TYPE>, Iterable<TYPE> {
    private final ConcurrentLinkedQueue<TYPE> queue = new ConcurrentLinkedQueue<>();
    private final Object lock = new Object();


    public Throwable getError() {
        return error;
    }

    public TYPE getFirst() {
        if (this.hasNext()) {
            return this.next();
        }
        return null;
    }

    public Optional<TYPE> getFirstOptional() {
        return Optional.ofNullable(this.getFirst());
    }

    public void ifPresent(Consumer<? super TYPE> action) {
        TYPE value = getFirst();
        if (value != null) {
            action.accept(value);
        }
    }

    /// StreamObserver
    /// StreamObserver
    /// StreamObserver
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

    /// Iterator
    /// Iterator
    /// Iterator
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

    /// Iterable
    /// Iterable
    /// Iterable

    @NonNull
    @NotNull
    @Override
    public Iterator<TYPE> iterator() {
        return this;
    }

}

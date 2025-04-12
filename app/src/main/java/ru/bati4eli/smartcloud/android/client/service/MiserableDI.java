package ru.bati4eli.smartcloud.android.client.service;

import android.util.Log;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class MiserableDI {

    private static Map<Class, Object> services = new HashMap<>();

    public static void initializeComponents() {
        set(new AuthInterceptor());
        // Without Auth
        setAsync(ManagedChannel.class, () -> ManagedChannelBuilder.forAddress("bati4eli.ru", 9090).usePlaintext().build());
        // With Auth
        setAsync(Channel.class, () -> ManagedChannelBuilder.forAddress("bati4eli.ru", 9090)
                .usePlaintext() // Если у вас не использует TLS
                .intercept(MiserableDI.get(AuthInterceptor.class)) // Добавляем интерцептор
                .idleTimeout(1, TimeUnit.SECONDS)
                .build());
        set(GrpcService.init());
    }

    public static <T> void setAsync(Class<T> serviceClass, Supplier<T> supplier) {
        services.put(serviceClass, supplier);
    }

    public static void set(Object service) {
        services.put(service.getClass(), service);
    }

    public static <T> T get(Class<T> serviceClass) {
        Object service = services.get(serviceClass);
        if (service == null) {
            T proxy = getProxy(serviceClass);
            services.put(serviceClass, proxy);
            return proxy;
        } else if (service instanceof Supplier) {
            Supplier<T> supplier = (Supplier) service;
            return supplier.get();
        }
        return (T) service;
    }

    /**
     * Работает только с интерфейсами
     */
    private static <T> T getProxy(Class<T> serviceClass) {
        // Создаем прокси-объект для оригинального сервиса
        try {
            T newProxyInstance = (T) Proxy.newProxyInstance(
                    serviceClass.getClassLoader(),
                    new Class<?>[]{serviceClass},
                    (proxy, method, args) -> {
                        // Логика перед вызовом метода
                        Log.d("serg", "# Before call method: " + method.getName());
                        // Получаем оригинальный сервис
                        T service = (T) services.get(serviceClass);
                        if (service == null) {
                            Log.e("serg", "Service not found: " + serviceClass.getName());
                        }
                        // Вызов оригинального метода
                        Object result = method.invoke(service, args);
                        // Логика после вызова метода
                        Log.d("serg", "# After call method: " + method.getName());
                        return result;
                    });
            return newProxyInstance;
        } catch (Exception e) {
            Log.e("serg", e.getMessage());
        }
        return null;
    }

}

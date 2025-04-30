package ru.bati4eli.smartcloud.android.client.service;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.joanzapata.iconify.fonts.MeteoconsModule;
import com.joanzapata.iconify.fonts.SimpleLineIconsModule;
import com.joanzapata.iconify.fonts.TypiconsModule;
import com.joanzapata.iconify.fonts.WeathericonsModule;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

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
        if (service instanceof Supplier) {
            Supplier<T> supplier = (Supplier) service;
            T t = supplier.get();
            return t;
        }
        return (T) service;
    }

}

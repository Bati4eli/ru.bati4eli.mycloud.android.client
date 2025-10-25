package ru.bati4eli.smartcloud.android.client;


import android.app.Application;
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
import lombok.val;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        logUncaughtException();

        Iconify
                .with(new FontAwesomeModule())
                .with(new EntypoModule())
                .with(new TypiconsModule())
                .with(new MaterialModule())
                .with(new MaterialCommunityModule())
                .with(new MeteoconsModule())
                .with(new WeathericonsModule())
                .with(new SimpleLineIconsModule())
                .with(new IoniconsModule())
        ;
    }


    /**
     * Логирование внезапных исключений.
     */
    private void logUncaughtException() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e) -> uncaughtException(defaultHandler, t, e));
    }

    public void uncaughtException(Thread.UncaughtExceptionHandler defaultHandler, Thread t, Throwable e) {
        // Логируем краткое описание
        android.util.Log.e("MyCloud", "Uncaught exception in thread: " + t.getName() + " (" + t.getId() + ")", e);

        // Передаём дальше системному обработчику, чтобы процесс корректно завершился
        if (defaultHandler != null) {
            defaultHandler.uncaughtException(t, e);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }
    }
}

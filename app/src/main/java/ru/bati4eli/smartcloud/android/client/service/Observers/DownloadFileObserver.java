package ru.bati4eli.smartcloud.android.client.service.Observers;

import android.util.Log;
import io.grpc.stub.StreamObserver;
import ru.bati4eli.mycloud.repo.DownloadFileResp;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.smartcloud.android.client.utils.Constants;
import ru.bati4eli.smartcloud.android.client.utils.MyUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class DownloadFileObserver implements StreamObserver<DownloadFileResp> {
    private AtomicBoolean working = new AtomicBoolean(true);
    private FileOutputStream fileOutputStream;
    private String filePath;

    public DownloadFileObserver(GrpcFile grpcFile, DownloadType downloadType) {
        mkdir("PREVIEWS");
        mkdir("ORIGIN");
        filePath = MyUtils.getFilePath(grpcFile, downloadType);
        try {
            fileOutputStream = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "DownloadFileObserver(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onNext(DownloadFileResp downloadFileResp) {
        // Получаем данные из ответа
        byte[] imageData = downloadFileResp.getData().toByteArray();
        // Сохраняем файл
        try {
            // Записываем данные в файл
            fileOutputStream.write(imageData);
            fileOutputStream.flush();
            //Toast.makeText(getApplicationContext(), "Изображение сохранено: " + filePath, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e(TAG, "Error writing to file: " + e.getMessage());
            //Toast.makeText(getApplicationContext(), "Ошибка при сохранении изображения", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        // Обработка ошибок
        Log.e(TAG, "DownloadFileObserver onError(): " + throwable.getMessage());
        working.set(false);
        //Toast.makeText(getApplicationContext(), "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show();
        closeStream();
    }

    @Override
    public void onCompleted()    {
        working.set(false);
        closeStream();
    }

    private void closeStream() {
        try {
            fileOutputStream.close();
        } catch (IOException ignored) {
        } finally {
        }
    }

    public boolean isWorking() {
        return working.get();
    }

    private static void mkdir(String PREVIEWS) {
        File directory = new File(Constants.APP_DIRECTORY, PREVIEWS);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public File waiting() {
        while (isWorking()) {
        }
        return new File(filePath);
    }
}

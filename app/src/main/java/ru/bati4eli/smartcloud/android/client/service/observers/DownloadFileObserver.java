package ru.bati4eli.smartcloud.android.client.service.observers;

import android.util.Log;
import ru.bati4eli.mycloud.repo.DownloadFileResp;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.smartcloud.android.client.model.ShortInfo;
import ru.bati4eli.smartcloud.android.client.utils.MyUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class DownloadFileObserver extends BaseStreamObserver<DownloadFileResp> {
    private FileOutputStream fileOutputStream;
    private String filePath;
    private Runnable onComplete;

    public DownloadFileObserver(ShortInfo info, DownloadType downloadType) {
        filePath = MyUtils.getFilePath(info, downloadType);
        try {
            fileOutputStream = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "DownloadFileObserver(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public DownloadFileObserver(ShortInfo info, DownloadType downloadType, Runnable onComplete) {
        this(info, downloadType);
        this.onComplete = onComplete;
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
    public void onCompleted() {
        working.set(false);
        closeStream();
        if (onComplete != null) {
            onComplete.run();
        }
    }

    private void closeStream() {
        try {
            fileOutputStream.close();
        } catch (IOException ignored) {
        } finally {
        }
    }

}

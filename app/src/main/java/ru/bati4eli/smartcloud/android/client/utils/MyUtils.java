package ru.bati4eli.smartcloud.android.client.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.mycloud.repo.TypeOfFile;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.model.ShortInfo;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;


public class MyUtils {

    public static void mkdir(String folderName) {
        File directory = new File(Constants.APP_DIRECTORY, folderName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static boolean previewExists(ShortInfo info, DownloadType downloadType) {
        return new File(getFilePath(info, downloadType)).exists();
    }

    public static String getFilePath(ShortInfo info, DownloadType downloadType) {
        if (downloadType == DownloadType.ORIGIN) {
            return Constants.APP_DIRECTORY + "/ORIGIN/" + info.getFileName();
        } else {
            return Constants.APP_DIRECTORY + "/PREVIEWS/" + info.getFileId() + "_" + downloadType.name() + ".jpg";
        }
    }

    public static void setupPreviewAsync(ShortInfo info, ImageView fileIcon, DownloadType downloadType) {
        try {
            // Повторно не надо скачивать превью
            if (MyUtils.previewExists(info, downloadType)) {
                setupPreview(info, fileIcon, downloadType);
                return;
            }
            MiserableDI.get(GrpcService.class)
                    .downloadFileAsync(info, downloadType, () -> setupPreview(info, fileIcon, downloadType));
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void setupPreview(ShortInfo info, ImageView fileIcon, DownloadType downloadType) {
        String filePath = MyUtils.getFilePath(info, downloadType);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        if (bitmap != null) {
            fileIcon.setImageBitmap(bitmap);
        } else {
            // Если загрузка не удалась, возможно, использовать иконку файла по умолчанию
            fileIcon.setImageResource(R.drawable.ic_file);
        }
    }

    /**
     * Расчет количества колонок в зависимости от ширины плитки
     */
    public static int calculateSpanCount(Fragment fragment, int tileWidthDp) {
        // Получаем ширину дисплея в пикселях
        DisplayMetrics displayMetrics = fragment.getResources().getDisplayMetrics();
        int displayWidth = displayMetrics.widthPixels;
        // Конвертируем ширину плитки из dp в пиксели
        float density = displayMetrics.density; // получаем плотность экрана
        // Высчитываем ширину плитки в пикселях
        int tileWidthPx = (int) (tileWidthDp * density);
        // Определяем количество колонок
        return Math.max(1, displayWidth / tileWidthPx);
    }

    public static double compareSeconds(Date dt1, Date dt2) {
        long difference = dt1.getTime() - dt2.getTime();
        return difference / 1000.0;
    }

    public static String formatDate(String dateStr) {
        // Форматируем дату и возвращаем строку
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            return parseDate(dateStr).format(formatter);
        } else {
            return dateStr;
        }
    }

    public static OffsetDateTime parseDate(String dateStr) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return OffsetDateTime.parse(dateStr);
            }
        } catch (Throwable e) {
            Log.e(TAG, "Error parsing date: " + dateStr);
            return null;
        }
        return null;
    }

    public static boolean isFolder(String folderPath) {
        File file = new File(folderPath);
        return file.exists() && file.isDirectory();
    }

    public static boolean isFile(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    public static void renameFileOrDirectory(String oldPath, String newName) {
        try {
            File fileSys = new File(oldPath);
            if (isFile(oldPath)) {
                File parentDir = fileSys.getParentFile();
                File newFile = new File(parentDir, newName);
                boolean isRenamed = fileSys.renameTo(newFile);
                if (isRenamed) {
                    System.out.println("^^^ Файл успешно переименован в: " + newFile.getPath());
                } else {
                    System.err.println("^^^ Не удалось переименовать файл.");
                }
            } else if (isFolder(oldPath)) {
                File parentDir = fileSys.getParentFile();
                File newFolder = new File(parentDir, newName);
                boolean isRenamed = fileSys.renameTo(newFolder);
                if (isRenamed) {
                    System.out.println("^^^ Папка успешно переименована в: " + newFolder.getPath());
                } else {
                    System.err.println("^^^ Не удалось переименовать папку.");
                }
            } else {
                System.err.println("^^^ Указанный файл или папка не существует.");
            }
        } catch (Exception e) {
            System.err.println("^^^ Произошла ошибка: " + e.getMessage());
        }
    }

    public static Date dateTimeParse(String dateTime) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
            return format.parse(dateTime);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Set<String> getSubDiskFiles(String folderPath) {
        Set<String> fileNames = new HashSet<>();
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    fileNames.add(file.getName());
                }
            }
        }

        return fileNames;
    }

    public static boolean fileSystemInfoExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static File getFileSystemInfo(String path) {
        return new File(path);
    }

    public static File getParentDirectory(String path) {
        File file = new File(path);
        return file.getParentFile();
    }

    public static String removeRootInPath(String path) {
        String rootPrefix = "/ROOT";
        if (path != null && path.startsWith(rootPrefix)) {
            return path.substring(rootPrefix.length()).replaceFirst("^/", "");
        }
        return path;
    }

    public static String simplifyPath(String fullPath, String prefix) {
        if (prefix == null) {
            prefix = ParametersUtil.getMainFolder();
        }

        if (fullPath.startsWith(prefix)) {
            String simplifiedPath = fullPath.substring(prefix.length());
            if (simplifiedPath.startsWith("/")) {
                simplifiedPath = simplifiedPath.substring(1);
            }
            return simplifiedPath;
        }
        return fullPath;
    }

    public static void updateFileDateTime(String path, Date created, Date lastModify) {
//        File file = new File(path);
//        if (file.exists()) {
//            try {
//                if (file.isFile()) {
//                    BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
//                    // Установить дату изменения (lastModified).
//                    file.setLastModified(lastModify.getTime());
//                    // Обратите внимание, что установка даты создания зависит от конкретных библиотек или версий ОС.
//                } else if (file.isDirectory()) {
//                    file.setLastModified(lastModify.getTime());
//                }
//            } catch (Exception ex) {
//                System.err.println("Ошибка при изменении времени файла: " + ex.getMessage());
//            }
//        } else {
//            System.err.println("^^^ Файл или папка не найдены.");
//        }
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static String getFullPath(GrpcFile grpcFile) {
        String remoteParentPath = removeRootInPath(grpcFile.getParentPath());
        String mainFolder = ParametersUtil.getMainFolder();

        if (grpcFile.getMediaType() == TypeOfFile.ROOT) {
            return mainFolder;
        }

        if (remoteParentPath == null || remoteParentPath.isEmpty()) {
            return new File(mainFolder, grpcFile.getName()).getPath();
        }

        return new File(mainFolder, remoteParentPath + File.separator + grpcFile.getName()).getPath();
    }
}

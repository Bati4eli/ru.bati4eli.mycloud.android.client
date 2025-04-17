package ru.bati4eli.smartcloud.android.client.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.mycloud.repo.TypeOfFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;


public class MyUtils {

    public static void mkdir(String folderName) {
        File directory = new File(Constants.APP_DIRECTORY, folderName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static boolean previewExists(GrpcFile grpcFile, DownloadType downloadType) {
        return new File(getFilePath(grpcFile, downloadType)).exists();
    }

    public static String getFilePath(GrpcFile grpcFile, DownloadType downloadType) {
        if (downloadType == DownloadType.ORIGIN) {
            return Constants.APP_DIRECTORY + "/ORIGIN/" + grpcFile.getName();
        } else {
            return Constants.APP_DIRECTORY + "/PREVIEWS/" + grpcFile.getFileId() + "_" + downloadType.name() + ".jpg";
        }
    }

    public static double compareSeconds(Date dt1, Date dt2) {
        long difference = dt1.getTime() - dt2.getTime();
        return difference / 1000.0;
    }

    public static String formatDate(Date dateTime) {
        // Получаем текущую временную зону
        TimeZone localZone = TimeZone.getDefault();

        // Преобразуем дату в строку локального времени
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
        dateFormat.setTimeZone(localZone);

        // Добавляем смещение часового пояса
        int offset = localZone.getRawOffset() + (localZone.inDaylightTime(dateTime) ? localZone.getDSTSavings() : 0);
        int offsetHours = offset / (60 * 60 * 1000);
        int offsetMinutes = Math.abs(offset / (60 * 1000)) % 60;

        return dateFormat.format(dateTime) + String.format(Locale.getDefault(), "%+03d:%02d", offsetHours, offsetMinutes);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formatDate(OffsetDateTime date) {
        // Задаем нужный формат даты
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        // Форматируем дату и возвращаем строку
        return date.format(formatter);
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

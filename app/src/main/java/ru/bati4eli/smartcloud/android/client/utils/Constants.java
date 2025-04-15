package ru.bati4eli.smartcloud.android.client.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class Constants {
    public static final String TAG = "MyCloud";
    public static final String DCIM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    public static File APP_DIRECTORY = null;

    public static void setAppDirectory(File filesDir) {
        APP_DIRECTORY = filesDir;
        Log.d(TAG, "setAppDirectory: " + APP_DIRECTORY);
    }
}

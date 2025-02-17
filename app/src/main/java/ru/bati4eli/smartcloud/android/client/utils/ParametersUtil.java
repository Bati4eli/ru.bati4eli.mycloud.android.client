package ru.bati4eli.smartcloud.android.client.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import lombok.SneakyThrows;

import java.io.File;

public class ParametersUtil {
    private static final String EMPTY_FOLDER = "";

    @SneakyThrows
    private static SharedPreferences getSharedPreferences(Context context) {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        return EncryptedSharedPreferences.create(
                context,
                "secret_shared_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public static void setSecrets(Context context, String username, String password, String token) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("token", token);
        editor.apply();
    }

    public static String getPassword(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString("password", null);
    }

    public static String getUsername(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString("username", null);
    }

    public static void setToken(Context context, String token) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString("token", null);
    }

    public static void setMainFolder(Context context, String folder) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("MainFolder", folder);
        editor.apply();
    }

    public static String getMainFolder(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString("MainFolder", EMPTY_FOLDER);
    }

    public static File getMainFolderFileInfo(Context context) {
        String mainFolder = getMainFolder(context);
        if (mainFolder == null || mainFolder.isEmpty()) {
            // Можно заменить на ваш путь, если нужно, или обработать ошибку в соответствии с вашей логикой.
            // throw new NullPointerException("MainFolder is null!!");
            throw new IllegalStateException("MainFolder is null!!");
        }
        return new File(mainFolder);
    }

    public static boolean needSetupPage(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean("NeedSetupPage", true);
    }

    public static void setNeedSetupPage(Context context, boolean needSetupPage) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("NeedSetupPage", needSetupPage);
        editor.apply();
    }

    public static boolean showTabBar(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean("ShowTabBar", true);
    }
}

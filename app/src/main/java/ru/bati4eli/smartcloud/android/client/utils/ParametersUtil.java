package ru.bati4eli.smartcloud.android.client.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;

public class ParametersUtil {
    private static final String EMPTY_FOLDER = "";

    @Setter
    private static Context context;

    public static void setSecrets( String username, String password, String token) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("token", token);
        editor.apply();
    }

    public static String getPassword() {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString("password", null);
    }

    public static String getUsername() {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString("username", null);
    }

    public static void setToken( String token) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public static String getToken() {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString("token", null);
    }

    public static void setMainFolder( String folder) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("MainFolder", folder);
        editor.apply();
    }

    public static String getMainFolder() {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString("MainFolder", EMPTY_FOLDER);
    }

    public static File getMainFolderFileInfo(Context context) {
        String mainFolder = getMainFolder();
        if (mainFolder == null || mainFolder.isEmpty()) {
            // Можно заменить на ваш путь, если нужно, или обработать ошибку в соответствии с вашей логикой.
            // throw new NullPointerException("MainFolder is null!!");
            throw new IllegalStateException("MainFolder is null!!");
        }
        return new File(mainFolder);
    }

    public static boolean getNeedSetupPage() {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean("NeedSetupPage", true);
    }

    public static void setNeedSetupPage( boolean needSetupPage) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("NeedSetupPage", needSetupPage);
        editor.apply();
    }

    public static boolean showTabBar() {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean("ShowTabBar", true);
    }

    public static void setNeedSplitsByYears(boolean isNeedYears) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("NeedSpliteByYears", isNeedYears);
        editor.apply();
    }

    public static boolean getNeedSplitsByYears() {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean("NeedSpliteByYears", true);
    }

    public static void setNeedScreenshots(boolean isNeedYears) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("NeedScreenshots", isNeedYears);
        editor.apply();
    }

    public static boolean getNeedScreenshots() {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean("NeedScreenshots", true);
    }

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
}

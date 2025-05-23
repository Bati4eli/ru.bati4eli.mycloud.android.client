package ru.bati4eli.smartcloud.android.client.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import lombok.Setter;
import lombok.SneakyThrows;
import ru.bati4eli.smartcloud.android.client.enums.GroupNameEnum;
import ru.bati4eli.smartcloud.android.client.enums.SortByEnum;
import ru.bati4eli.smartcloud.android.client.enums.SortOrderEnum;
import ru.bati4eli.smartcloud.android.client.enums.ViewTypeEnum;

import java.util.HashMap;
import java.util.Map;


public class ParametersUtil {
    private static final String EMPTY_FOLDER = "";
    public static final String NEED_SETUP_PAGE = "NeedSetupPage";
    public static final String NEED_SPLITE_BY_YEARS = "NeedSpliteByYears";
    public static final String NEED_SCREENSHOTS = "NeedScreenshots";
    public static final String MAIN_FOLDER = "MainFolder";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";

    @Setter
    private static Context context;

    private static final Map<GroupNameEnum, Integer> cache = new HashMap<>();

    public static void setSecrets(String username, String password, String token) {
        SharedPreferences preferences = getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.putString(TOKEN, token);
        editor.apply();
    }

    public static int getSortParam(GroupNameEnum groupName) {
        Integer value = cache.get(groupName);
        if (value != null) {
            return value;
        }

        switch (groupName) {
            case VIEW_TYPE:
                value = getSharedPreferences().getInt(GroupNameEnum.VIEW_TYPE.name(), ViewTypeEnum.VIEW_LIST.getParameterId());
                break;
            case SORT_BY:
                value = getSharedPreferences().getInt(GroupNameEnum.SORT_BY.name(), SortByEnum.SORT_BY_CREATE_DATE.getParameterId());
                break;
            case SORT_ORDER:
                value = getSharedPreferences().getInt(GroupNameEnum.SORT_ORDER.name(), SortOrderEnum.SORT_ASCENDING.getParameterId());
                break;
        }
        cache.put(groupName, value);
        return value;
    }

    public static void setSortParam(GroupNameEnum groupName, Integer value) {
        saveParam(groupName.name(), value);
        cache.put(groupName, value);
    }

    public static ViewTypeEnum getViewType() {
        return ViewTypeEnum.of(getSortParam(GroupNameEnum.VIEW_TYPE));
    }

    public static SortByEnum getSortBy() {
        return SortByEnum.of(getSortParam(GroupNameEnum.SORT_BY));
    }

    public static SortOrderEnum getSortOrder() {
        return SortOrderEnum.of(getSortParam(GroupNameEnum.SORT_ORDER));
    }

    public static String getPassword() {
        return getSharedPreferences().getString(PASSWORD, null);
    }

    public static String getUsername() {
        return getSharedPreferences().getString(USERNAME, null);
    }

    public static void setToken(String token) {
        saveParam(TOKEN, token);
    }

    public static String getToken() {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getString(TOKEN, null);
    }

    public static void setMainFolder(String folder) {
        saveParam(MAIN_FOLDER, folder);
    }


    public static String getMainFolder() {
        return getSharedPreferences().getString(MAIN_FOLDER, EMPTY_FOLDER);
    }

    public static boolean getNeedSetupPage() {
        return getSharedPreferences().getBoolean(NEED_SETUP_PAGE, true);
    }

    public static void setNeedSetupPage(boolean needSetupPage) {
        saveParam(NEED_SETUP_PAGE, needSetupPage);
    }

    public static void setNeedSplitsByYears(boolean isNeedYears) {
        saveParam(NEED_SPLITE_BY_YEARS, isNeedYears);
    }

    public static boolean getNeedSplitsByYears() {
        return getSharedPreferences().getBoolean(NEED_SPLITE_BY_YEARS, true);
    }

    public static void setNeedScreenshots(boolean isNeedYears) {
        saveParam(NEED_SCREENSHOTS, isNeedYears);
    }

    public static boolean getNeedScreenshots() {
        return getSharedPreferences().getBoolean(NEED_SCREENSHOTS, true);
    }


    private static void saveParam(String paraName, Integer defaultValue) {
        SharedPreferences preferences = getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(paraName, defaultValue);
        editor.apply();
    }

    private static void saveParam(String paraName, String defaultValue) {
        SharedPreferences preferences = getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(paraName, defaultValue);
        editor.apply();
    }

    private static void saveParam(String paraName, boolean defaultValue) {
        SharedPreferences preferences = getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(paraName, defaultValue);
        editor.apply();
    }

    @SneakyThrows
    private static SharedPreferences getSharedPreferences() {
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

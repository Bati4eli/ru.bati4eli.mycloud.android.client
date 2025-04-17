package ru.bati4eli.smartcloud.android.client.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import lombok.Setter;
import lombok.SneakyThrows;
import ru.bati4eli.smartcloud.android.client.enums.SortByEnum;
import ru.bati4eli.smartcloud.android.client.enums.SortOrderEnum;
import ru.bati4eli.smartcloud.android.client.enums.ViewTypeEnum;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtil {
    private static final String EMPTY_FOLDER = "";
    public static final String VIEW_TYPE = "viewType";
    public static final String SORT_BY = "sortBy";
    public static final String SORT_ORDER = "sortOrder";
    public static final String NEED_SETUP_PAGE = "NeedSetupPage";
    public static final String SHOW_TAB_BAR = "ShowTabBar";
    public static final String NEED_SPLITE_BY_YEARS = "NeedSpliteByYears";
    public static final String NEED_SCREENSHOTS = "NeedScreenshots";
    public static final String MAIN_FOLDER = "MainFolder";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";

    @Setter
    private static Context context;

    private static Map<String, Integer> cache = new HashMap<>();

    public static void setSecrets(String username, String password, String token) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.putString(TOKEN, token);
        editor.apply();
    }

    public static int getSortParam(String groupName) {
        Integer value = cache.get(groupName);
        if (value != null) {
            return value;
        }

        if (groupName.equals(VIEW_TYPE)) {
            value = getViewType().getParameterId();
        } else if (groupName.equals(SORT_BY)) {
            value = getSortBy().getParameterId();
        } else if (groupName.equals(SORT_ORDER)) {
            value = getSortOrder().getParameterId();
        }
        cache.put(groupName, value);
        return value;
    }

    public static void setSortParam(String groupName, Integer value) {
        saveParam(groupName, value);
        cache.put(groupName, value);
    }

    public static ViewTypeEnum getViewType() {
        int anInt = getSharedPreferences(context).getInt(VIEW_TYPE, ViewTypeEnum.VIEW_LIST.getParameterId());
        return ViewTypeEnum.of(anInt);
    }

    public static SortByEnum getSortBy() {
        int anInt = getSharedPreferences(context).getInt(SORT_BY, SortByEnum.SORT_BY_CREATE_DATE.getParameterId());
        return SortByEnum.of(anInt);
    }

    public static SortOrderEnum getSortOrder() {
        int anInt = getSharedPreferences(context).getInt(SORT_ORDER, SortOrderEnum.SORT_ASCENDING.getParameterId());
        return SortOrderEnum.of(anInt);
    }

    public static String getPassword() {
        return getSharedPreferences(context).getString(PASSWORD, null);
    }

    public static String getUsername() {
        return getSharedPreferences(context).getString(USERNAME, null);
    }

    public static void setToken(String token) {
        saveParam(TOKEN, token);
    }

    public static String getToken() {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(TOKEN, null);
    }

    public static void setMainFolder(String folder) {
        saveParam(MAIN_FOLDER, folder);
    }


    public static String getMainFolder() {
        return getSharedPreferences(context).getString(MAIN_FOLDER, EMPTY_FOLDER);
    }

    public static boolean getNeedSetupPage() {
        return getSharedPreferences(context).getBoolean(NEED_SETUP_PAGE, true);
    }

    public static void setNeedSetupPage(boolean needSetupPage) {
        saveParam(NEED_SETUP_PAGE, needSetupPage);
    }

    public static boolean showTabBar() {
        return getSharedPreferences(context).getBoolean(SHOW_TAB_BAR, true);
    }

    public static void setNeedSplitsByYears(boolean isNeedYears) {
        saveParam(NEED_SPLITE_BY_YEARS, isNeedYears);
    }

    public static boolean getNeedSplitsByYears() {
        return getSharedPreferences(context).getBoolean(NEED_SPLITE_BY_YEARS, true);
    }

    public static void setNeedScreenshots(boolean isNeedYears) {
        saveParam(NEED_SCREENSHOTS, isNeedYears);
    }

    public static boolean getNeedScreenshots() {
        return getSharedPreferences(context).getBoolean(NEED_SCREENSHOTS, true);
    }


    private static void saveParam(String paraName, Integer defaultValue) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(paraName, defaultValue);
        editor.apply();
    }

    private static void saveParam(String paraName, String defaultValue) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(paraName, defaultValue);
        editor.apply();
    }

    private static void saveParam(String paraName, boolean defaultValue) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(paraName, defaultValue);
        editor.apply();
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

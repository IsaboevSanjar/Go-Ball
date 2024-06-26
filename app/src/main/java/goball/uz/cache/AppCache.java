package goball.uz.cache;

import android.content.Context;
import android.content.SharedPreferences;

public class AppCache {
    private static AppCache appCache;
    private final SharedPreferences preferences;
    private final String USER_TOKEN = "token";
    private final String USER_HEADER = "header";
    private final String PHONE_NUMBER = "phone_number";
    private final String FULL_NAME = "full_name";

    private AppCache(Context context) {
        preferences = context.getSharedPreferences("AppCache", Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (appCache == null) {
            appCache = new AppCache(context);
        }
    }

    public static AppCache getHelper() {
        return appCache;
    }

    public String getToken() {
        return preferences.getString(USER_TOKEN, null);
    }

    public void setToken(String token) {
        preferences.edit().putString(USER_TOKEN, token).apply();
    }

    public String getPhoneNumber() {
        return preferences.getString(PHONE_NUMBER, null);
    }

    public void setPhoneNumber(String token) {
        preferences.edit().putString(PHONE_NUMBER, token).apply();
    }

    public String getFullName() {
        return preferences.getString(FULL_NAME, null);
    }

    public void setFullName(String token) {
        preferences.edit().putString(FULL_NAME, token).apply();
    }

    public String getHeader() {
        return preferences.getString(USER_HEADER, null);
    }

    public void setHeader(String token) {
        preferences.edit().putString(USER_HEADER, token).apply();
    }

    public Boolean getFirstOpen() {
        return preferences.getBoolean("isFirst", true);
    }

    public void setFirstOpen(Boolean open) {
        preferences.edit().putBoolean("isFirst", open).apply();
    }
}

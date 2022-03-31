package in.processmaster.salestripclm.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceClass {

    public static final String PREFERENCE_NAME = "PREFERENCE_DATA";
    private final SharedPreferences sharedpreferences;

    public PreferenceClass(Context context) {
        sharedpreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public String getPref(String key) {
        String count = sharedpreferences.getString(key, "");
        return count;
    }

    public Boolean checkKeyExist(String key)
    {
        Boolean exist = sharedpreferences.contains(key);
        return exist;
    }

    public Boolean getPrefBool(String key) {
        Boolean count = sharedpreferences.getBoolean(key, false);
        return count;
    }

    public void setPref(String key, String value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
        editor.apply();
    }
    public void setPrefBool(String key, boolean value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
        editor.apply();
    }

    public void clearCount(String key) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(key);
        editor.commit();
    }
}

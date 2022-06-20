package org.isfp.housemate;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    private static final String filename = "FirebaseChatting";

    private static SharedPreferences getPreference(Context context){
        return context.getSharedPreferences(filename, Context.MODE_PRIVATE);
    }
//    static SharedPreferences getSharedPreferences(Context context) {
//        return PreferenceManager.getDefaultSharedPreferences(context);
//    }
    public static void setValue(Context context, String key, String value){
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(key, value);
        editor.commit();

    }

    public static void setValue(Context context, String key, Integer value){
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static String getStringValue(Context context, String key){
        return getPreference(context).getString(key, "");
    }

    public static Integer getIntegerValue(Context context, String key){
        return getPreference(context).getInt(key, 0);
    }

    public static void clearUser(Context context) {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.clear();
        editor.commit();
    }
}

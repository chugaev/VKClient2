package com.anatoly.chugaev.VKAPP.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Account {
    private static Account sAccount;
    public String access_token;
    public long user_id;

    private Account(Context context) {
        restore(context);
    }

    public static Account get(Context context) {
        if (sAccount == null) {
            sAccount = new Account(context);
        }
        return sAccount;
    }

    public void save(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("access_token", access_token);
        editor.putLong("user_id", user_id);
        editor.apply();
    }

    public void restore(Context context) {
        SharedPreferences prefs;
        if (context != null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
            access_token = prefs.getString("access_token", null);
            user_id = prefs.getLong("user_id", 0);
        } else {
            Log.w("START", "NULL!!!");
        }
    }
}

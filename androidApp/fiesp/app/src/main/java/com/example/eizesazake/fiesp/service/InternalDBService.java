package com.example.eizesazake.fiesp.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.eizesazake.fiesp.sqlite.InternalDBSqlite;

public class InternalDBService {

    // Handle to SharedPreferences for Switch onToggleClicked
    private SharedPreferences mPrefs;
    // Handle to a SharedPreferences editor
    private SharedPreferences.Editor mEditor;

    // Name of shared preferences repository that stores persistent state
    public static final String SHARED_PREFERENCES =
            "android.loginOrSignupService.SHARED_PREFERENCES";

    // Key for storing the "updates requested" flag in shared preferences
    public static final String LATLONG =
            ".android.loginOrSignupService.LATLONG";

    public static final String ALARM =
            ".android.loginOrSignupService.ALARM";
    public static final String ADDRESS =
            ".android.loginOrSignupService.ADDRESS";

    public InternalDBService(Context context) {
        // Open Shared Preferences
        mPrefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        // Get an editor
        mEditor = mPrefs.edit();
    }

    public static boolean sqliteAddUserLogin(
            InternalDBSqlite internalDBSqlite,
            String latlong) {
        internalDBSqlite.deleteLatlong();
        // if sqlite is empty, it will add user login info
//        if (loginSqlite.getUserLoginCount(loginSqlite) == 0) {
            // adds user login info to sqlite
        internalDBSqlite.addData(latlong);
            return true;
//        } else {
//            return false;
//        }
    }

    public SharedPreferences getmPrefs() {
        return mPrefs;
    }

    public void setmPrefs(SharedPreferences mPrefs) {
        this.mPrefs = mPrefs;
    }

    public SharedPreferences.Editor getmEditor() {
        return mEditor;
    }

    public void setmEditor(SharedPreferences.Editor mEditor) {
        this.mEditor = mEditor;
    }


}


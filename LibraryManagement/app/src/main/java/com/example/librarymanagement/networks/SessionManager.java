package com.example.librarymanagement.networks;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static String TAG = SessionManager.class.getName();
    SharedPreferences preferences;
    Context context;
    SharedPreferences.Editor editor;
    private static int PRE_MODE = 1;
    private static final String NAME = "android_demo";
    private static final String KEY_LOGIN = "islogin";
    private static final String USER = "user";
    private static final String ROLE ="role";

    public SessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setUser(int user){
        editor.putInt(USER, user);
        editor.commit();
    }

    public int getUser(){
        return preferences.getInt(USER, 0);
    }

    public void setRole(int role){
        editor.putInt(ROLE, role);
        editor.commit();
    }

    public int getRole(){
        return preferences.getInt(ROLE, 0);
    }

    public void setLogin(boolean isLogin){
        editor.putBoolean(KEY_LOGIN, isLogin);
        editor.commit();
    }
    public boolean check(){
        return preferences.getBoolean(KEY_LOGIN, false);
    }
    public void clear(){
        editor.clear();
        editor.commit();
    }
}

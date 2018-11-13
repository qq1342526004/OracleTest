package com.hasee.onlinedb;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class App extends Application {
    private static App instance;
    private SharedPreferences preferences ;
    public static App getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        preferences = getSharedPreferences("databaseLinkInfo", Context.MODE_PRIVATE);
    }

    //取出本地的数据
    public String getPreferences(){
        SharedPreferences preferences = getSharedPreferences("databaseLinkInfo",MODE_PRIVATE);
        String message = preferences.getString("databaseLinkInfo","");
        return message;
    }
}

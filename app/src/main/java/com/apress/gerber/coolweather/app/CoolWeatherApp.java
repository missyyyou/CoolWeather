package com.apress.gerber.coolweather.app;

import android.app.Application;

public class CoolWeatherApp extends Application {
    public static CoolWeatherApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}

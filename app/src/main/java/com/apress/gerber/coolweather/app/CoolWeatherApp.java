package com.apress.gerber.coolweather.app;

import android.app.Application;

/**
 * 作者：Neil on 2017/8/2 15:57.
 * 邮箱：cn.neillee@gmail.com
 */

public class CoolWeatherApp extends Application {
    public static CoolWeatherApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}

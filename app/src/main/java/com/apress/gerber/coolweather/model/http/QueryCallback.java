package com.apress.gerber.coolweather.model.http;

import com.apress.gerber.coolweather.model.bean.WeatherInfo;

public interface QueryCallback {
    void onSuccess(WeatherInfo weatherInfo);

    void onFail(String errMsg);
}

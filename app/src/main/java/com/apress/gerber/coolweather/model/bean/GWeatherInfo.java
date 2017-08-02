package com.apress.gerber.coolweather.model.bean;

public class GWeatherInfo {
    /**
     * city : 陇西
     * cityid : 101160203
     * temp1 : -2℃
     * temp2 : 16℃
     * weather : 多云转晴
     * img1 : n1.gif
     * img2 : d0.gif
     * ptime : 18:00
     */
    private WeatherInfo weatherinfo;

    public WeatherInfo getWeatherinfo() {
        return weatherinfo;
    }

    public void setWeatherinfo(WeatherInfo weatherinfo) {
        this.weatherinfo = weatherinfo;
    }
}

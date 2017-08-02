package com.apress.gerber.coolweather.model.bean;

public class CountyInfo {
    private String mCountyCode;
    private String mCountyWeatherCode;

    public CountyInfo(String countyCode, String countyWeatherCode) {
        mCountyCode = countyCode;
        mCountyWeatherCode = countyWeatherCode;
    }

    public String getCountyCode() {
        return mCountyCode;
    }

    public void setCountyCode(String countyCode) {
        mCountyCode = countyCode;
    }

    public String getCountyWeatherCode() {
        return mCountyWeatherCode;
    }

    public void setCountyWeatherCode(String countyWeatherCode) {
        mCountyWeatherCode = countyWeatherCode;
    }
}

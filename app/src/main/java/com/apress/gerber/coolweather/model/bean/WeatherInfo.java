package com.apress.gerber.coolweather.model.bean;

public class WeatherInfo {
    private String mCityName;
    private String mMaxTemp;
    private String mMinTemp;
    private String mPubTime;
    private String mCurDate;
    private String mWeatherDesc;

    public WeatherInfo() {
    }

    public WeatherInfo(String cityName, String maxTemp, String minTemp,
                       String pubTime, String curDate, String weatherDesc) {
        mCityName = cityName;
        mMaxTemp = maxTemp;
        mMinTemp = minTemp;
        mPubTime = pubTime;
        mCurDate = curDate;
        mWeatherDesc = weatherDesc;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public String getMaxTemp() {
        return mMaxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        mMaxTemp = maxTemp;
    }

    public String getMinTemp() {
        return mMinTemp;
    }

    public void setMinTemp(String minTemp) {
        mMinTemp = minTemp;
    }

    public String getPubTime() {
        return mPubTime;
    }

    public void setPubTime(String pubTime) {
        mPubTime = pubTime;
    }

    public String getCurDate() {
        return mCurDate;
    }

    public void setCurDate(String curDate) {
        mCurDate = curDate;
    }

    public String getWeatherDesc() {
        return mWeatherDesc;
    }

    public void setWeatherDesc(String weatherDesc) {
        mWeatherDesc = weatherDesc;
    }
}

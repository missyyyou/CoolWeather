package com.apress.gerber.coolweather.model.bean;

import com.google.gson.annotations.SerializedName;

public class WeatherInfo {
    @SerializedName("city")
    private String cityName;
    // 注意：这里使用的 cityid 其实是CountyWeatherCode
    @SerializedName("cityid")
    private String cityId;
    @SerializedName("temp1")
    private String minTemp;
    @SerializedName("temp2")
    private String maxTemp;
    @SerializedName("weather")
    private String weatherDesc;
    private String img1;
    private String img2;
    @SerializedName("ptime")
    private String pubTime;

    public WeatherInfo(String cityName, String cityId, String minTemp,
                       String maxTemp, String weatherDesc, String pubTime) {
        this.cityName = cityName;
        this.cityId = cityId;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.weatherDesc = weatherDesc;
        this.pubTime = pubTime;
    }

    public WeatherInfo(String cityName, String cityId, String minTemp,
                       String maxTemp, String weatherDesc,
                       String img1, String img2, String pubTime) {
        this.cityName = cityName;
        this.cityId = cityId;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.weatherDesc = weatherDesc;
        this.img1 = img1;
        this.img2 = img2;
        this.pubTime = pubTime;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getWeatherDesc() {
        return weatherDesc;
    }

    public void setWeatherDesc(String weatherDesc) {
        this.weatherDesc = weatherDesc;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }
}

package com.apress.gerber.coolweather.model.bean;

public class County extends District {
    private int id;
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return districtName;
    }

    public void setCountyName(String countyName) {
        this.districtName = countyName;
    }

    public String getCountyCode() {
        return districtCode;
    }

    public void setCountyCode(String countyCode) {
        this.districtCode = countyCode;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}

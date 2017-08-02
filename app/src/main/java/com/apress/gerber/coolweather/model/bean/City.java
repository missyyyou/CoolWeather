package com.apress.gerber.coolweather.model.bean;

public class City extends District {
    private int id;
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return districtName;
    }

    public void setCityName(String cityName) {
        this.districtName = cityName;
    }

    public String getCityCode() {
        return districtCode;
    }

    public void setCityCode(String cityCode) {
        this.districtCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}

package com.apress.gerber.coolweather.model.bean;

public class Province extends District {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return districtName;
    }

    public void setProvinceName(String provinceName) {
        this.districtName = provinceName;
    }

    public String getProvinceCode() {
        return districtCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.districtCode = provinceCode;
    }
}

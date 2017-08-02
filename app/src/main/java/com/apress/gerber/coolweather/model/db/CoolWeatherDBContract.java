package com.apress.gerber.coolweather.model.db;

/**
 * 作者：missyyyou on 2017/8/3 07:19.
 * 邮箱：yysha-94-03@foxmail.com
 */
public class CoolWeatherDBContract {
    public static final int VERSION = 1;
    public static final String DB_NAME = "cool_weather";

    public static final String PROVINCE_TABLE_NAME = "Province";
    public static final String PROVINCE_TABLE_PRIMARY_KEY = "id";
    public static final String PROVINCE_TABLE_KEY_NAME = "province_name";
    public static final String PROVINCE_TABLE_KEY_CODE = "province_code";

    public static final String CITY_TABLE_NAME = "City";
    public static final String CITY_TABLE_PRIMARY_KEY = "id";
    public static final String CITY_TABLE_KEY_NAME = "city_name";
    public static final String CITY_TABLE_KEY_CODE = "city_code";
    public static final String CITY_TABLE_KEY_PROVINCE_ID = "province_id";

    public static final String COUNTY_TABLE_NAME = "County";
    public static final String COUNTY_TABLE_PRIMARY_KEY = "id";
    public static final String COUNTY_TABLE_KEY_NAME = "county_name";
    public static final String COUNTY_TABLE_KEY_CODE = "county_code";
    public static final String COUNTY_TABLE_KEY_CITY_ID = "city_id";
}

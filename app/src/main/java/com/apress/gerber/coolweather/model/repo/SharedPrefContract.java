package com.apress.gerber.coolweather.model.repo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.apress.gerber.coolweather.model.bean.CountyInfo;
import com.apress.gerber.coolweather.model.bean.WeatherInfo;

/**
 * 使用{@link SharedPreferences}存储当前{@link WeatherInfo}和最新{@link CountyInfo}的操作类
 */
public class SharedPrefContract {
    private static final String SP_CITY_SELECTED = "city_selected";
    private static final String SP_CITY_NAME = "city_name";
    private static final String SP_CITY_ID = "weather_code";
    private static final String SP_MAX_TEMP = "temp1";
    private static final String SP_MIN_TEMP = "temp2";
    private static final String SP_WEATHER_DESC = "weather_desc";
    private static final String SP_PUB_TIME = "publish_time";

    /**
     * 保存当前选中的城市天气信息
     *
     * @param context     上下文对象
     * @param weatherInfo 天气信息
     */
    public static void saveWeatherInfo(Context context, WeatherInfo weatherInfo) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(SP_CITY_SELECTED, true);
        editor.putString(SP_CITY_NAME, weatherInfo.getCityName());
        editor.putString(SP_CITY_ID, weatherInfo.getCityId());
        editor.putString(SP_MAX_TEMP, weatherInfo.getMaxTemp());
        editor.putString(SP_MIN_TEMP, weatherInfo.getMinTemp());
        editor.putString(SP_WEATHER_DESC, weatherInfo.getWeatherDesc());
        editor.putString(SP_PUB_TIME, weatherInfo.getPubTime());
        editor.apply();
    }

    /**
     * 获取当前保存的城市天气信息
     *
     * @param context 上下文对象
     */
    public static WeatherInfo getWeatherInfo(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isCitySelected = sp.getBoolean(SP_CITY_SELECTED, true);
        if (!isCitySelected) return null;
        String cityName = sp.getString(SP_CITY_NAME, "");
        String cityId = sp.getString(SP_CITY_ID, "");
        String maxTemp = sp.getString(SP_MAX_TEMP, "");
        String minTemp = sp.getString(SP_MIN_TEMP, "");
        String weatherDesc = sp.getString(SP_WEATHER_DESC, "");
        String pubTime = sp.getString(SP_PUB_TIME, "");
        return new WeatherInfo(cityName, cityId, minTemp, maxTemp, weatherDesc, pubTime);
    }

    private static final String SP_COUNTY_CODE = "county_code";
    private static final String SP_COUNTY_WEATHER_CODE = "county_weather_code";

    /**
     * 保存当前选中的城市气象索引信息
     *
     * @param context    上下文对象
     * @param countyInfo 城市信息
     */
    public static void saveRecentCountyInfo(Context context, CountyInfo countyInfo) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(SP_COUNTY_CODE, countyInfo.getCountyCode());
        editor.putString(SP_COUNTY_WEATHER_CODE, countyInfo.getCountyWeatherCode());
        editor.apply();
    }

    /**
     * 获取当前保存的城市气象索引信息
     *
     * @param context 上下文对象
     */
    public static CountyInfo getRecentCountyInfo(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String countyCode = sp.getString(SP_COUNTY_CODE, "");
        String countyWeatherCode = sp.getString(SP_COUNTY_WEATHER_CODE, "");
        if (countyCode.equals("") || countyWeatherCode.equals("")) return null;
        else return new CountyInfo(countyCode, countyWeatherCode);
    }
}

package com.apress.gerber.coolweather.model.repo;

import android.support.annotation.NonNull;

import com.apress.gerber.coolweather.model.bean.City;
import com.apress.gerber.coolweather.model.bean.County;
import com.apress.gerber.coolweather.model.bean.CountyInfo;
import com.apress.gerber.coolweather.model.bean.Province;
import com.apress.gerber.coolweather.model.bean.WeatherInfo;

import java.util.List;

/**
 * 作者：missyyyou on 2017/8/3 13:47.
 * 邮箱：yysha-94-03@foxmail.com
 */

public interface LocalDataSink {
    void saveProvinces(@NonNull List<Province> provinceList);

    void saveCities(@NonNull List<City> cityList);

    void saveCounties(@NonNull List<County> countyList);

    void saveRecentCountyInfo(@NonNull CountyInfo info);

    void saveWeatherInfo(@NonNull WeatherInfo info);
}

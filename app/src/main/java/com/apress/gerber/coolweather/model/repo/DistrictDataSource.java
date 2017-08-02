package com.apress.gerber.coolweather.model.repo;

import android.support.annotation.NonNull;

import com.apress.gerber.coolweather.model.bean.City;
import com.apress.gerber.coolweather.model.bean.County;
import com.apress.gerber.coolweather.model.bean.CountyInfo;
import com.apress.gerber.coolweather.model.bean.District;
import com.apress.gerber.coolweather.model.bean.Province;
import com.apress.gerber.coolweather.model.bean.WeatherInfo;

import java.util.List;

/**
 * 数据获取接口
 */
public interface DistrictDataSource {
    /**
     * 省市县列表回调接口
     *
     * @param <T> 省市县
     */
    interface LoadDistrictsCallback<T extends District> {
        void onDistrictsLoaded(List<T> districts);

        void onDataNotAvailable();
    }

    /**
     * {@link WeatherInfo}回调接口
     */
    interface LoadWeatherInfoCallback {
        void onWeatherInfoLoaded(WeatherInfo weatherInfo);

        void onDataNotAvailable();
    }

    /**
     * {@link CountyInfo}回调接口
     */
    interface LoadCountyInfoCallback {
        void onCountyInfoLoaded(CountyInfo countyInfo);

        void onDataNotAvailable();
    }

    /**
     * 加载 省一级 列表
     *
     * @param callback 使用回调返回数据
     */
    void loadProvinces(@NonNull LoadDistrictsCallback<Province> callback);

    /**
     * 加载 市一级 列表
     *
     * @param proCode  省编号
     * @param callback 使用回调返回数据
     */
    void loadCities(int proCode, @NonNull LoadDistrictsCallback<City> callback);

    /**
     * 加载 县一级 列表
     *
     * @param cityCode 市编号
     * @param callback 使用回调返回数据
     */
    void loadCounties(int cityCode, @NonNull LoadDistrictsCallback<County> callback);

    /**
     * 加载 {@link CountyInfo}
     *
     * @param countyCode 县编号
     * @param callback   使用回调返回数据
     */
    void loadCountyInfo(int countyCode, @NonNull LoadCountyInfoCallback callback);

    /**
     * 加载 {@link WeatherInfo}
     *
     * @param countyCode        县编号
     * @param countyWeatherCode 县气象编号
     * @param callback          使用回调返回数据
     */
    void loadWeatherInfo(int countyCode, int countyWeatherCode, @NonNull LoadWeatherInfoCallback callback);
}

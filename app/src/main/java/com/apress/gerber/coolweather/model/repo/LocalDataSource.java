package com.apress.gerber.coolweather.model.repo;

import android.content.Context;
import android.support.annotation.NonNull;

import com.apress.gerber.coolweather.model.bean.City;
import com.apress.gerber.coolweather.model.db.CoolWeatherDB;
import com.apress.gerber.coolweather.model.bean.County;
import com.apress.gerber.coolweather.model.bean.CountyInfo;
import com.apress.gerber.coolweather.model.bean.Province;
import com.apress.gerber.coolweather.model.bean.WeatherInfo;

import java.util.List;

/**
 * 本地源加载数据类
 * Provinces、Cities、Counties从数据库中进行加载
 * CountyInfo、WeatherInfo使用{@link SharedPrefContract}从SharedPreference中进行加载
 */
public class LocalDataSource implements DistrictDataSource, LocalDataSink {
    private CoolWeatherDB mDBUtils;
    private Context mContext;

    public LocalDataSource(CoolWeatherDB DBUtils, Context context) {
        mDBUtils = DBUtils;
        mContext = context;
    }

    /**
     * 从数据库中加载 省一级 列表
     *
     * @param callback 使用回掉返回数据
     */
    @Override
    public void loadProvinces(@NonNull LoadDistrictsCallback<Province> callback) {
        callback.onDistrictsLoaded(mDBUtils.loadProvince());
    }

    /**
     * 从数据库中加载 市一级 列表
     *
     * @param proCode  省编号
     * @param callback 使用回掉返回数据
     */
    @Override
    public void loadCities(int proCode, @NonNull LoadDistrictsCallback<City> callback) {
        callback.onDistrictsLoaded(mDBUtils.loadCities(proCode));
    }

    /**
     * 从数据库中加载 县一级 列表
     *
     * @param cityCode 市编号
     * @param callback 使用回掉返回数据
     */
    @Override
    public void loadCounties(int cityCode, @NonNull LoadDistrictsCallback<County> callback) {
        callback.onDistrictsLoaded(mDBUtils.loadCounties(cityCode));
    }

    /**
     * 从{@link SharedPrefContract#getRecentCountyInfo(Context)} SharedPreference 中加载县的信息（县编号及县气象编号）
     * 若参数 countyCode 与缓存的 countyCode 不一致，则获取失败
     *
     * @param countyCode 县编号
     * @param callback   使用回掉返回数据
     */
    @Override
    public void loadCountyInfo(int countyCode, @NonNull LoadCountyInfoCallback callback) {
        CountyInfo countyInfo = SharedPrefContract.getRecentCountyInfo(mContext);
        if (countyInfo != null && countyInfo.getCountyCode().equals(countyCode + ""))
            callback.onCountyInfoLoaded(countyInfo);
        else callback.onDataNotAvailable();
    }

    /**
     * 从{@link SharedPrefContract#getRecentCountyInfo(Context)} SharedPreference 中加载县的天气信息
     * 若参数 countyWeatherCode 与缓存的 countyWeatherCode 不一致，则获取失败
     *
     * @param countyCode        县编号，无实际用处
     * @param countyWeatherCode 县天气索引编号
     * @param callback          使用回掉返回数据
     */
    @Override
    public void loadWeatherInfo(int countyCode, int countyWeatherCode, @NonNull LoadWeatherInfoCallback callback) {
        WeatherInfo info = SharedPrefContract.getWeatherInfo(mContext);
        if (info != null && info.getCityId().equals(countyCode + ""))
            callback.onWeatherInfoLoaded(info);
        else callback.onDataNotAvailable();
    }

    @Override
    public void saveProvinces(@NonNull List<Province> provinceList) {
        for (Province pro : provinceList) mDBUtils.saveProvince(pro);
    }

    @Override
    public void saveCities(@NonNull List<City> cityList) {
        for (City city : cityList) mDBUtils.saveCity(city);
    }

    @Override
    public void saveCounties(@NonNull List<County> countyList) {
        for (County county : countyList) mDBUtils.saveCounty(county);
    }

    @Override
    public void saveRecentCountyInfo(@NonNull CountyInfo info) {
        SharedPrefContract.saveRecentCountyInfo(mContext, info);
    }

    @Override
    public void saveWeatherInfo(@NonNull WeatherInfo info) {
        SharedPrefContract.saveWeatherInfo(mContext, info);
    }
}

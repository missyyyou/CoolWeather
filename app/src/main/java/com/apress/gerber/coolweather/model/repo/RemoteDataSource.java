package com.apress.gerber.coolweather.model.repo;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.apress.gerber.coolweather.model.bean.City;
import com.apress.gerber.coolweather.model.bean.County;
import com.apress.gerber.coolweather.model.bean.CountyInfo;
import com.apress.gerber.coolweather.model.bean.District;
import com.apress.gerber.coolweather.model.bean.GWeatherInfo;
import com.apress.gerber.coolweather.model.bean.Province;
import com.apress.gerber.coolweather.model.bean.WeatherInfo;
import com.apress.gerber.coolweather.util.HttpCallbackListener;
import com.apress.gerber.coolweather.util.HttpUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 远程源加载数据类
 * 从网络进行数据加载
 */
public class RemoteDataSource implements DistrictDataSource {
    private static final String PROVINCE_LIST_URL = "http://www.weather.com.cn/data/list3/city.xml";
    private static final String CITY_LIST_URL_PLACE_HOLDER = "http://www.weather.com.cn/data/list3/city%02d.xml";
    private static final String COUNTY_LIST_URL_PLACE_HOLDER = "http://www.weather.com.cn/data/list3/city%04d.xml";
    private static final String COUNTY_INFO_URL_PLACE_HOLDER = "http://www.weather.com.cn/data/list3/city%06d.xml";
    private static final String WEATHER_INFO_URL_PLACE_HOLDER = "http://www.weather.com.cn/data/cityinfo/%09d.html";

    /**
     * 从网络加载 省一级 列表
     * 返回的数据格式为 “01|北京,02|上海,03|天津,04|重庆,05|黑龙江,...” ，使用{@link #joinDistrictList}进行数据处理
     *
     * @param callback 使用回掉进行数据返回
     */
    @Override
    public void loadProvinces(@NonNull final LoadDistrictsCallback<Province> callback) {
        HttpUtil.sendHttpRequest(PROVINCE_LIST_URL, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                List<Province> provinceList = joinDistrictList(Province.class, response);
                if (provinceList != null) callback.onDistrictsLoaded(provinceList);
                else callback.onDataNotAvailable();
            }

            @Override
            public void onError(Exception e) {
                callback.onDataNotAvailable();
            }
        });
    }

    /**
     * 从网络加载 市一级 列表
     * 返回的数据格式为 “1601|兰州,1602|定西,1603|平凉,1604|庆阳,1605|武威,...” ，使用{@link #joinDistrictList}进行数据处理
     *
     * @param proCode  省编号
     * @param callback 使用回掉进行数据返回
     */
    @Override
    public void loadCities(int proCode, @NonNull final LoadDistrictsCallback<City> callback) {
        String address = String.format(CITY_LIST_URL_PLACE_HOLDER, proCode);
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                List<City> cityList = joinDistrictList(City.class, response);
                if (cityList != null) callback.onDistrictsLoaded(cityList);
                else callback.onDataNotAvailable();
            }

            @Override
            public void onError(Exception e) {
                callback.onDataNotAvailable();
            }
        });
    }

    /**
     * 从网络加载 县一级 列表
     * 返回的数据格式为 “160201|定西,160202|通渭,160203|陇西,...” ，使用{@link #joinDistrictList}进行数据处理
     *
     * @param cityCode 市编号
     * @param callback 使用回掉进行数据返回
     */
    @Override
    public void loadCounties(int cityCode, @NonNull final LoadDistrictsCallback<County> callback) {
        String address = String.format(COUNTY_LIST_URL_PLACE_HOLDER, cityCode);
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                List<County> countyList = joinDistrictList(County.class, response);
                if (countyList != null) callback.onDistrictsLoaded(countyList);
                else callback.onDataNotAvailable();
            }

            @Override
            public void onError(Exception e) {
                callback.onDataNotAvailable();
            }
        });
    }

    /**
     * 从网络加载 县信息
     * 返回的数据格式为 “160203|101160203” ，前一数字为该县编号，后一数字为县的气象编号，使用{@link #joinDistrictList}进行数据处理
     *
     * @param countyCode 县编号
     * @param callback   使用回掉进行数据返回
     */
    @Override
    public void loadCountyInfo(int countyCode, @NonNull final LoadCountyInfoCallback callback) {
        String address = String.format(COUNTY_INFO_URL_PLACE_HOLDER, countyCode);
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                CountyInfo info = null;
                if (!TextUtils.isEmpty(response)) {
                    String[] array = response.split("\\|");
                    if (array.length == 2) {
                        info = new CountyInfo(array[0], array[1]);
                    }
                }
                if (info != null) callback.onCountyInfoLoaded(info);
                else callback.onDataNotAvailable();
            }

            @Override
            public void onError(Exception e) {
                callback.onDataNotAvailable();
            }
        });
    }

    /**
     * 从网络加载 县的气象信息
     * 返回的数据格式为 JSON 格式，对应的实体类为{@link GWeatherInfo}
     *
     * @param countyCode        县编号
     * @param countyWeatherCode 县的气象编号
     * @param callback          使用回掉进行数据返回
     */
    @Override
    public void loadWeatherInfo(int countyCode, int countyWeatherCode, @NonNull final LoadWeatherInfoCallback callback) {
        String address = String.format(WEATHER_INFO_URL_PLACE_HOLDER, countyWeatherCode);
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Gson gson = new Gson();
                GWeatherInfo gInfo = gson.fromJson(response, GWeatherInfo.class);
                WeatherInfo info = gInfo != null ? gInfo.getWeatherinfo() : null;
                if (info != null) callback.onWeatherInfoLoaded(info);
                else callback.onDataNotAvailable();
            }

            @Override
            public void onError(Exception e) {
                callback.onDataNotAvailable();
            }
        });
    }

    /**
     * 用于处理省市县列表数据的 List 化
     *
     * @param c        省市县的类型
     * @param response 用于处理的数据
     * @param <T>      省市县的类型
     * @return 省市县列表
     */
    private <T extends District> List<T> joinDistrictList(Class<T> c, String response) {
        List<T> provinceList = null;
        try {
            if (!TextUtils.isEmpty(response)) {
                String[] allProvinces = response.split(",");
                if (allProvinces.length > 0) {
                    provinceList = new ArrayList<>();
                    for (String p : allProvinces) {
                        String[] array = p.split("\\|");
                        T t = c.newInstance();
                        t.setDistrictCode(array[0]);
                        t.setDistrictName(array[1]);
                        provinceList.add(t);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provinceList;
    }
}

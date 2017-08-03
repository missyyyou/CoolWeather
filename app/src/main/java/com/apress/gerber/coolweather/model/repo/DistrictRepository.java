package com.apress.gerber.coolweather.model.repo;

import android.support.annotation.NonNull;

import com.apress.gerber.coolweather.model.bean.City;
import com.apress.gerber.coolweather.model.bean.County;
import com.apress.gerber.coolweather.model.bean.CountyInfo;
import com.apress.gerber.coolweather.model.bean.Province;
import com.apress.gerber.coolweather.model.bean.WeatherInfo;

import java.util.List;

/**
 * 应用数据的缓存、获取类
 * 该类作用为：1.缓存数据（{@link #mProvinceList}{@link #mCityList} {@link #mCountyList}）；2.根据参数（{@link #mForceRefreshing}{@link #mCacheIsDirty}）执行获取数据的操作（网络{@link #mRemoteDataSource}/本地{@link #mLocalDataSource}）
 */
public class DistrictRepository implements DistrictDataSource {
    /**
     * {@link #mProvinceList}、{@link #mCityList}、{@link #mCountyList}表中的信息永远不可能是脏的数据
     */
    private List<Province> mProvinceList;
    private List<City> mCityList;
    private List<County> mCountyList;

    /**
     * 该参数影响到{@link #mCachedCounty}、{@link #mCachedCountyInfo}、{@link #mCachedWeatherInfo}的加载动作
     */
    private boolean mForceRefreshing = true;

    /**
     * {@link #mCachedCounty}、{@link #mCachedCountyInfo}、{@link #mCachedWeatherInfo}可以为脏数据
     */
    private County mCachedCounty;
    private CountyInfo mCachedCountyInfo;
    private WeatherInfo mCachedWeatherInfo;
    /**
     * 该参数只会影响到{@link #mCachedCounty}、{@link #mCachedCountyInfo}、{@link #mCachedWeatherInfo}的加载动作
     */
    private boolean mCacheIsDirty = true;

    private RemoteDataSource mRemoteDataSource;
    private LocalDataSource mLocalDataSource;

    private static DistrictRepository sInstance;

    public static DistrictRepository getInstance(RemoteDataSource remoteDataSource,
                                                 LocalDataSource localDataSource) {
        if (sInstance == null)
            sInstance = new DistrictRepository(remoteDataSource, localDataSource);
        return sInstance;
    }

    public DistrictRepository(RemoteDataSource remoteDataSource,
                              LocalDataSource localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
    }

    /**
     * 加载 省一级 列表
     *
     * @param callback 使用回调返回数据
     */
    @Override
    public void loadProvinces(@NonNull final LoadDistrictsCallback<Province> callback) {
        if (mProvinceList != null) {
            callback.onDistrictsLoaded(mProvinceList);
        } else {
            mLocalDataSource.loadProvinces(new LoadDistrictsCallback<Province>() {
                @Override
                public void onDistrictsLoaded(List<Province> districts) {
                    mProvinceList = districts;
                    if (districts == null || districts.size() <= 0)
                        getRemoteProvinceList(callback);
                    else callback.onDistrictsLoaded(districts);
                }

                @Override
                public void onDataNotAvailable() {
                    getRemoteProvinceList(callback);
                }
            });
        }
    }

    /**
     * 加载 市一级 列表
     *
     * @param proCode  省编号
     * @param callback 使用回调返回数据
     */
    @Override
    public void loadCities(final int proCode, @NonNull final LoadDistrictsCallback<City> callback) {
        if (mCityList != null) {
            callback.onDistrictsLoaded(mCityList);
        } else {
            mLocalDataSource.loadCities(proCode, new LoadDistrictsCallback<City>() {
                @Override
                public void onDistrictsLoaded(List<City> districts) {
                    mCityList = districts;
                    if (districts == null || districts.size() <= 0)
                        getRemoteCityList(proCode, callback);
                    else callback.onDistrictsLoaded(districts);
                }

                @Override
                public void onDataNotAvailable() {
                    getRemoteCityList(proCode, callback);
                }
            });
        }
    }

    /**
     * 加载 县一级 列表
     *
     * @param cityCode 县编号
     * @param callback 使用回调返回数据
     */
    @Override
    public void loadCounties(final int cityCode, @NonNull final LoadDistrictsCallback<County> callback) {
        if (mCountyList != null) {
            callback.onDistrictsLoaded(mCountyList);
        } else {
            mLocalDataSource.loadCounties(cityCode, new LoadDistrictsCallback<County>() {
                @Override
                public void onDistrictsLoaded(List<County> districts) {
                    mCountyList = districts;
                    if (districts == null || districts.size() <= 0)
                        getRemoteCountyList(cityCode, callback);
                    else callback.onDistrictsLoaded(districts);
                }

                @Override
                public void onDataNotAvailable() {
                    getRemoteCountyList(cityCode, callback);
                }
            });
        }
    }

    /**
     * 加载县的详情（包含县编号和县气象编号{@link CountyInfo}），加载动作同时受{@link #mForceRefreshing}和{@link #mCacheIsDirty}影响
     *
     * @param countyCode 县编号
     * @param callback   使用回调返回数据
     */
    @Override
    public void loadCountyInfo(final int countyCode, @NonNull final LoadCountyInfoCallback callback) {
        if (mCachedCounty != null && mCachedCountyInfo != null                              // 有缓存
                && mCachedCounty.getCountyCode().equals(mCachedCountyInfo.getCountyCode())  // 缓存一致
                && !mCacheIsDirty && !mForceRefreshing) {                                   // 没有其他要求
            callback.onCountyInfoLoaded(mCachedCountyInfo);
        }
        if (mCacheIsDirty || mForceRefreshing) {
            getRemoteCountyInfo(countyCode, callback);
        } else {
            mLocalDataSource.loadCountyInfo(countyCode, new LoadCountyInfoCallback() {
                @Override
                public void onCountyInfoLoaded(CountyInfo countyInfo) {
                    mCachedCountyInfo = countyInfo;
                    callback.onCountyInfoLoaded(countyInfo);
                }

                @Override
                public void onDataNotAvailable() {
                    getRemoteCountyInfo(countyCode, callback);
                }
            });
        }
    }

    /**
     * 加载县的气象信息，加载动作同时受{@link #mForceRefreshing}和{@link #mCacheIsDirty}影响
     * 两种情况：1.强制刷新，执行一步请求；2.脏数据和其他情况，执行两步请求
     *
     * @param countyCode        县编号
     * @param countyWeatherCode 县的气象编号
     * @param callback          使用回调返回数据
     */

    @Override
    public void loadWeatherInfo(final int countyCode, final int countyWeatherCode, @NonNull final LoadWeatherInfoCallback callback) {
        if (mCachedCounty != null && mCachedCountyInfo != null && mCachedWeatherInfo != null    // Cached 值是否为空
                && mCachedCounty.getCountyCode().equals(mCachedCountyInfo.getCountyCode())
                && mCachedCountyInfo.getCountyCode().equals(mCachedWeatherInfo.getCityId())     // Cached 的值是否统一
                && !mCacheIsDirty && !mForceRefreshing) {                                       // 没有其他要求
            callback.onWeatherInfoLoaded(mCachedWeatherInfo);
        }
        if (!mCacheIsDirty && mForceRefreshing) {   // 不是脏数据，仅仅是需要强制刷新，执行一步请求
            getRemoteWeatherInfo(countyCode, countyWeatherCode, callback);
        } else if (mCacheIsDirty) {                 // 脏数据，需要执行两步请求
            getRemoteCountyInfo(countyCode, new LoadCountyInfoCallback() {
                @Override
                public void onCountyInfoLoaded(CountyInfo countyInfo) {
                    int countyCode = Integer.valueOf(countyInfo.getCountyCode());
                    int countyWeatherCode = Integer.valueOf(countyInfo.getCountyWeatherCode());
                    getRemoteWeatherInfo(countyCode, countyWeatherCode, callback);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        }
    }

    /**
     * 封装了网络获取 CountyInfo
     *
     * @param countyCode 县编号
     * @param callback   使用回调返回数据
     */
    private void getRemoteCountyInfo(int countyCode, final LoadCountyInfoCallback callback) {
        mRemoteDataSource.loadCountyInfo(countyCode, new LoadCountyInfoCallback() {
            @Override
            public void onCountyInfoLoaded(CountyInfo countyInfo) {
                mCachedCountyInfo = countyInfo;
                mLocalDataSource.saveRecentCountyInfo(countyInfo);
                if (mCachedCountyInfo != null) {
                    callback.onCountyInfoLoaded(countyInfo);
                } else callback.onDataNotAvailable();
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    /**
     * 封装了网络获取 WeatherInfo
     *
     * @param countyCode        县编号
     * @param countyWeatherCode 县的气象编号
     * @param callback          使用回调返回数据
     */
    private void getRemoteWeatherInfo(int countyCode, int countyWeatherCode, final LoadWeatherInfoCallback callback) {
        mRemoteDataSource.loadWeatherInfo(countyCode, countyWeatherCode, new LoadWeatherInfoCallback() {
            @Override
            public void onWeatherInfoLoaded(WeatherInfo weatherInfo) {
                mCachedWeatherInfo = weatherInfo;
                mLocalDataSource.saveWeatherInfo(weatherInfo);
                mForceRefreshing = false;
                mCacheIsDirty = false;
                callback.onWeatherInfoLoaded(weatherInfo);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    /**
     * 封装了网络获取 ProvinceList
     *
     * @param callback 使用回调返回数据
     */
    private void getRemoteProvinceList(@NonNull final LoadDistrictsCallback<Province> callback) {
        mRemoteDataSource.loadProvinces(new LoadDistrictsCallback<Province>() {
            @Override
            public void onDistrictsLoaded(List<Province> districts) {
                mForceRefreshing = false;
                mProvinceList = districts;
                if (mProvinceList != null && mProvinceList.size() > 0) {
                    mLocalDataSource.saveProvinces(mProvinceList);
                    callback.onDistrictsLoaded(districts);
                } else callback.onDataNotAvailable();
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    /**
     * 封装了网络获取 CityList
     *
     * @param proCode  省编号
     * @param callback 使用回调返回数据
     */
    private void getRemoteCityList(int proCode, @NonNull final LoadDistrictsCallback<City> callback) {
        mRemoteDataSource.loadCities(proCode, new LoadDistrictsCallback<City>() {
            @Override
            public void onDistrictsLoaded(List<City> districts) {
                mForceRefreshing = false;
                mCityList = districts;
                if (mCityList != null && mCityList.size() > 0) {
                    mLocalDataSource.saveCities(mCityList);
                    callback.onDistrictsLoaded(districts);
                } else callback.onDataNotAvailable();

            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    /**
     * 封装了网络获取 CountyList
     *
     * @param cityCode 市编号
     * @param callback 使用回调返回数据
     */
    private void getRemoteCountyList(int cityCode, @NonNull final LoadDistrictsCallback<County> callback) {
        mRemoteDataSource.loadCounties(cityCode, new LoadDistrictsCallback<County>() {
            @Override
            public void onDistrictsLoaded(List<County> districts) {
                mForceRefreshing = false;
                mCountyList = districts;
                if (mCountyList != null && mCountyList.size() > 0) {
                    mLocalDataSource.saveCounties(mCountyList);
                    callback.onDistrictsLoaded(districts);
                } else callback.onDataNotAvailable();
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    public void setCachedDirty() {
        mCacheIsDirty = true;
    }

    public void setForceRefreshing() {
        mForceRefreshing = true;
    }
}

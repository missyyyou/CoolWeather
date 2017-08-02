package com.apress.gerber.coolweather.model.http;

import com.apress.gerber.coolweather.model.bean.WeatherInfo;
import com.apress.gerber.coolweather.util.HttpCallbackListener;
import com.apress.gerber.coolweather.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 作者：Neil on 2017/8/2 11:07.
 * 邮箱：cn.neillee@gmail.com
 */

public class WeatherQuery {
    private static WeatherQuery mInstance;

    private WeatherQuery() {
    }

    public static WeatherQuery getInstance() {
        if (mInstance == null) {
            synchronized (WeatherQuery.class) {
                if (mInstance == null) mInstance = new WeatherQuery();
                return mInstance;
            }
        }
        return mInstance;
    }

    private void queryCountyWeather(final String address, final QueryCallback queryCallback) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
                    String cityName = weatherInfo.getString("city");
                    String weatherCode = weatherInfo.getString("cityid");
                    String maxTemp = weatherInfo.getString("temp1");
                    String minTemp = weatherInfo.getString("temp2");
                    String pubTime = weatherInfo.getString("ptime");
                    String weatherDesc = weatherInfo.getString("weather");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
                    String curDate = sdf.format(new Date());
                    queryCallback.onSuccess(new WeatherInfo(cityId, cityName,
                            maxTemp, minTemp, pubTime, curDate, weatherDesc));
                } catch (JSONException e) {
                    e.printStackTrace();
                    queryCallback.onFail(e.getMessage());
                }
                queryCallback.onSuccess(null);
            }

            @Override
            public void onError(Exception e) {
                queryCallback.onFail(e.getMessage());
            }
        });
    }
}

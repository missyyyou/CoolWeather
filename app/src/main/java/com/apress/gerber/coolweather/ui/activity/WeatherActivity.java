package com.apress.gerber.coolweather.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apress.gerber.coolweather.R;
import com.apress.gerber.coolweather.model.bean.CountyInfo;
import com.apress.gerber.coolweather.model.bean.WeatherInfo;
import com.apress.gerber.coolweather.model.repo.DistrictDataSource;
import com.apress.gerber.coolweather.model.repo.DistrictRepository;
import com.apress.gerber.coolweather.model.repo.LocalDataSource;
import com.apress.gerber.coolweather.model.repo.RemoteDataSource;
import com.apress.gerber.coolweather.model.repo.SharedPrefContract;
import com.apress.gerber.coolweather.service.AutoUpdateService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.apress.gerber.coolweather.ui.activity.ChooseAreaActivity.EXTRAS_COUNTY_CODE;

/**
 * 作者：missyyyou on 2017/3/15 07:55.
 * 邮箱：yysha-94-03@foxmail.com
 * 这是主Activity，内部包含跳转逻辑
 */
public class WeatherActivity extends Activity implements View.OnClickListener {
    public static final String EXTRAS_FROM_WEATHER_ACTY = "from_weather_activity";
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView publishText;
    private TextView cityNameText;
    private TextView currentDataText;
    private TextView weatherDescText;
    private LinearLayout weatherInfoLayout;

    private DistrictRepository mRepo;
    private CountyInfo mCountyInfo;
    private int mRequestCode = 0x10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_weather);

        boolean citySelected = initEvent();
        initViews();
        if (citySelected) { // 已存储有城市点位信息
            initData(mCountyInfo);
        } else { // 未存储城市点位信息
            Intent intent = new Intent(this, ChooseAreaActivity.class);
            startActivityForResult(intent, mRequestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mRequestCode == requestCode && resultCode == RESULT_OK) {
            String countyCode = data.getStringExtra(EXTRAS_COUNTY_CODE);
            initData(new CountyInfo(countyCode, "-1"));
        }
    }

    /**
     * 事件判断
     *
     * @return 是否已存在城市信息
     */
    private boolean initEvent() {
        mCountyInfo = SharedPrefContract.getRecentCountyInfo(this);
        return mCountyInfo != null;
    }

    private void initData(CountyInfo countyInfo) {
        String countyCode = countyInfo.getCountyCode();
        String countyWeatherCode = countyInfo.getCountyWeatherCode();
        if (!TextUtils.isEmpty(countyCode)) {
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.VISIBLE);

            if (mRepo == null) {
                LocalDataSource localDS = new LocalDataSource(null, this);
                RemoteDataSource remoteDS = new RemoteDataSource();
                mRepo = new DistrictRepository(remoteDS, localDS);
            }
            queryWeatherInfo(countyCode, countyWeatherCode, mRepo);
        } else {
            showWeather(SharedPrefContract.getWeatherInfo(this));
        }
    }

    private void initViews() {
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDescText = (TextView) findViewById(R.id.weather_desc);
        currentDataText = (TextView) findViewById(R.id.current_date);
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);

        Button switchCity = (Button) findViewById(R.id.switch_city);
        Button refreshWeather = (Button) findViewById(R.id.refresh_weather);

        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_city:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra(EXTRAS_FROM_WEATHER_ACTY, true);
                startActivity(intent);
                break;
            case R.id.refresh_weather:
                publishText.setText("同步中...");
                CountyInfo info = SharedPrefContract.getRecentCountyInfo(this);
                if (info != null) {
                    String countyCode = info.getCountyCode();
                    String countyWeatherCode = info.getCountyWeatherCode();
                    queryWeatherInfo(countyCode, countyWeatherCode, mRepo);
                }
                break;
            default:
                break;
        }
    }

    private void queryWeatherInfo(String countyCode, String countyWeatherCode, DistrictRepository repo) {
        int countyCodeInt = Integer.valueOf(countyCode);
        int countyWeatherCodeInt = Integer.valueOf(countyWeatherCode);
        repo.loadWeatherInfo(countyCodeInt, countyWeatherCodeInt,
                new DistrictDataSource.LoadWeatherInfoCallback() {
                    @Override
                    public void onWeatherInfoLoaded(final WeatherInfo weatherInfo) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showWeather(weatherInfo);
                            }
                        });
                    }

                    @Override
                    public void onDataNotAvailable() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showError();
                            }
                        });
                    }
                });
    }

    private void showWeather(WeatherInfo weatherInfo) {
        cityNameText.setText(weatherInfo.getCityName());
        temp1Text.setText(weatherInfo.getMinTemp());
        temp2Text.setText(weatherInfo.getMaxTemp());
        weatherDescText.setText(weatherInfo.getWeatherDesc());
        publishText.setText("今天" + weatherInfo.getPubTime() + "发布");
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        currentDataText.setText(format.format(new Date()));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    private void showError() {
        publishText.setText("同步失败");
    }
}

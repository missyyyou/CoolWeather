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
 */
public class WeatherActivity extends Activity implements View.OnClickListener {
    public static final String EXTRAS_FROM_WEATHER_ACTY = "from_weather_activity";
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView publishText;
    private TextView cityNameText;
    private TextView currentDataText;
    private LinearLayout weatherInfoLayout;

    private DistrictRepository mRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_weather);

        initViews();
        initData();
    }

    private void initData() {
        String countyCode = getIntent().getStringExtra(EXTRAS_COUNTY_CODE);

        if (!TextUtils.isEmpty(countyCode)) {
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.VISIBLE);

            LocalDataSource localDS = new LocalDataSource(null, this);
            RemoteDataSource remoteDS = new RemoteDataSource();
            mRepo = new DistrictRepository(remoteDS, localDS);
            queryWeatherInfo(countyCode, -1, mRepo);
        } else {
            showWeather(SharedPrefContract.getWeatherInfo(this));
        }
    }

    private void initViews() {
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDataText = (TextView) findViewById(R.id.current_date);

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
                finish();
                break;
            case R.id.refresh_weather:
                publishText.setText("同步中...");
                CountyInfo info = SharedPrefContract.getRecentCountyInfo(this);
                if (info != null) {
                    String countyCode = info.getCountyCode();
                    int countyWeatherCode = Integer.valueOf(info.getCountyWeatherCode());
                    queryWeatherInfo(countyCode, countyWeatherCode, mRepo);
                }
                break;
            default:
                break;
        }
    }

    private void queryWeatherInfo(String countyCode, int countyWeatherCode, DistrictRepository repo) {
        int countyCodeInt = Integer.valueOf(countyCode);
        repo.loadWeatherInfo(countyCodeInt, countyWeatherCode, new DistrictDataSource.LoadWeatherInfoCallback() {
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
        // weatherDespText.setText(prefs.getString("weather_desp",""));
        publishText.setText("今天" + weatherInfo.getPubTime() + "发布");
        SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
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

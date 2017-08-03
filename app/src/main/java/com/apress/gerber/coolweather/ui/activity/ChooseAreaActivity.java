package com.apress.gerber.coolweather.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apress.gerber.coolweather.R;
import com.apress.gerber.coolweather.model.bean.City;
import com.apress.gerber.coolweather.model.bean.County;
import com.apress.gerber.coolweather.model.bean.District;
import com.apress.gerber.coolweather.model.bean.Province;
import com.apress.gerber.coolweather.model.db.CoolWeatherDB;
import com.apress.gerber.coolweather.model.repo.DistrictDataSource;
import com.apress.gerber.coolweather.model.repo.DistrictRepository;
import com.apress.gerber.coolweather.model.repo.LocalDataSource;
import com.apress.gerber.coolweather.model.repo.RemoteDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：missyyyou on 2017/3/14 07:55.
 * 邮箱：yysha-94-03@foxmail.com
 */
public class ChooseAreaActivity extends Activity {
    public static final String EXTRAS_COUNTY_CODE = "county_code";

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private TextView titleText;
    private ListView mListView;
    private ProgressDialog mProgressDialog;

    private DistrictsAdapter mDistrictsAdapter;
    private List<District> mDistrictList;

    private Province selectedProvince;
    private City selectedCity;

    private int currentLevel;

    private DistrictRepository mRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_area);

        initViews();
        initData();
        queryProvinces();
    }

    private void initData() {
        CoolWeatherDB coolWeatherDB = CoolWeatherDB.getInstance(this);
        LocalDataSource localDS = new LocalDataSource(coolWeatherDB, this);
        RemoteDataSource remoteDS = new RemoteDataSource();
        mRepo = new DistrictRepository(remoteDS, localDS);
    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        mDistrictList = new ArrayList<>();
        mDistrictsAdapter = new DistrictsAdapter(this, mDistrictList);
        mListView.setAdapter(mDistrictsAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = (Province) mDistrictList.get(index);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = (City) mDistrictList.get(index);
                    queryCounties();
                } else if (currentLevel == LEVEL_COUNTY) {
                    County county = (County) mDistrictList.get(index);
                    String countyCode = county.getCountyCode();
                    Intent intent = new Intent();
                    intent.putExtra(EXTRAS_COUNTY_CODE, countyCode);
                    setResult(RESULT_OK, intent);
                    ChooseAreaActivity.this.finish();
                }
            }
        });
    }

    private void queryProvinces() {
        showProgressDialog();
        mRepo.loadProvinces(new DistrictDataSource.LoadDistrictsCallback<Province>() {
            @Override
            public void onDistrictsLoaded(final List<Province> districts) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        mDistrictList.clear();
                        for (Province province : districts) {
                            mDistrictList.add(province);
                        }
                        mDistrictsAdapter.notifyDataSetChanged();
                        mListView.setSelection(0);
                        titleText.setText("中国");
                        currentLevel = LEVEL_PROVINCE;
                    }
                });
            }

            @Override
            public void onDataNotAvailable() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // cities in province
    private void queryCities() {
        showProgressDialog();
        int proCode = Integer.valueOf(selectedProvince.getProvinceCode());
        mRepo.loadCities(proCode, new DistrictDataSource.LoadDistrictsCallback<City>() {
            @Override
            public void onDistrictsLoaded(final List<City> districts) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        mDistrictList.clear();
                        for (City city : districts) {
                            mDistrictList.add(city);
                        }
                        mDistrictsAdapter.notifyDataSetChanged();
                        //            mListView.setSelection(0);
                        titleText.setText(selectedProvince.getProvinceName());
                        currentLevel = LEVEL_CITY;
                    }
                });
            }

            @Override
            public void onDataNotAvailable() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void queryCounties() {
        showProgressDialog();
        int cityCode = Integer.valueOf(selectedCity.getCityCode());
        mRepo.loadCounties(cityCode, new DistrictDataSource.LoadDistrictsCallback<County>() {
            @Override
            public void onDistrictsLoaded(final List<County> districts) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        mDistrictList.clear();
                        for (County county : districts) {
                            mDistrictList.add(county);
                        }
                        mDistrictsAdapter.notifyDataSetChanged();
                        mListView.setSelection(0);
                        titleText.setText(selectedCity.getCityName());
                        currentLevel = LEVEL_COUNTY;
                    }
                });
            }

            @Override
            public void onDataNotAvailable() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在加载...");
            mProgressDialog.setCanceledOnTouchOutside(false);

        }
        mProgressDialog.show();
    }

    private void closeProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTY) {
            queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        } else {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            ChooseAreaActivity.this.finish();
        }
    }
}
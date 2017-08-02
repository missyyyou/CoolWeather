package com.apress.gerber.coolweather.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.apress.gerber.coolweather.model.bean.City;
import com.apress.gerber.coolweather.model.bean.County;
import com.apress.gerber.coolweather.model.bean.Province;

import java.util.ArrayList;
import java.util.List;

import static com.apress.gerber.coolweather.model.db.CoolWeatherDBContract.*;

/**
 * 作者：missyyyou on 2017/3/13 07:27.
 * 邮箱：yysha-94-03@foxmail.com
 */
public class CoolWeatherDB {
    private static CoolWeatherDB sCoolWeatherDB;
    private SQLiteDatabase db;

    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(
                context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static CoolWeatherDB getInstance(Context context) {
        if (sCoolWeatherDB == null) {
            sCoolWeatherDB = new CoolWeatherDB(context);
        }
        return sCoolWeatherDB;
    }

    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put(PROVINCE_TABLE_KEY_NAME, province.getProvinceName());
            values.put(PROVINCE_TABLE_KEY_CODE, province.getProvinceCode());
            db.insert(PROVINCE_TABLE_NAME, null, values);
        }
    }

    public List<Province> loadProvince() {
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query(PROVINCE_TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex(PROVINCE_TABLE_PRIMARY_KEY)));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex(PROVINCE_TABLE_KEY_CODE)));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex(PROVINCE_TABLE_KEY_NAME)));
                list.add(province);

            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put(CITY_TABLE_KEY_NAME, city.getCityName());
            values.put(CITY_TABLE_KEY_CODE, city.getCityCode());
            values.put(CITY_TABLE_KEY_PROVINCE_ID, city.getProvinceId());
            db.insert(CITY_TABLE_NAME, null, values);

        }
    }

    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query(CITY_TABLE_NAME, null, CITY_TABLE_KEY_PROVINCE_ID + "=?",
                new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex(CITY_TABLE_PRIMARY_KEY)));
                city.setCityCode(cursor.getString(cursor.getColumnIndex(CITY_TABLE_KEY_CODE)));
                city.setCityName(cursor.getString(cursor.getColumnIndex(CITY_TABLE_KEY_NAME)));
                city.setProvinceId(provinceId);
                list.add(city);

            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put(COUNTY_TABLE_KEY_CODE, county.getCountyCode());
            values.put(COUNTY_TABLE_KEY_NAME, county.getCountyName());
            values.put(COUNTY_TABLE_KEY_CITY_ID, county.getCityId());
            db.insert(COUNTY_TABLE_NAME, null, values);
        }
    }

    public List<County> loadCounties(int cityId) {
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query(COUNTY_TABLE_NAME, null, COUNTY_TABLE_KEY_CITY_ID + "=?",
                new String[]{String.valueOf(cityId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex(COUNTY_TABLE_PRIMARY_KEY)));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex(COUNTY_TABLE_KEY_CODE)));
                county.setCountyName(cursor.getString(cursor.getColumnIndex(COUNTY_TABLE_KEY_NAME)));

                county.setCityId(cityId);
                list.add(county);
            }
            while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }
}
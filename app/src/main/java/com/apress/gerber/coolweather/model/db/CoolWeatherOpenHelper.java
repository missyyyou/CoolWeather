package com.apress.gerber.coolweather.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.apress.gerber.coolweather.model.db.CoolWeatherDBContract.*;

/**
 * 作者：missyyyou on 2017/3/13 07:18.
 * 邮箱：yysha-94-03@foxmail.com
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
    private static final String CREATE_PROVINCE = String.format(
            "create table %s(%s integer primary key autoincrement, %s text, %s text)",
            PROVINCE_TABLE_NAME, PROVINCE_TABLE_PRIMARY_KEY, PROVINCE_TABLE_KEY_NAME, PROVINCE_TABLE_KEY_CODE);

    private static final String CREATE_CITY = String.format(
            "create table %s(%s integer primary key autoincrement, %s text, %s text, %s integer)",
            CITY_TABLE_NAME, CITY_TABLE_PRIMARY_KEY, CITY_TABLE_KEY_NAME, CITY_TABLE_KEY_CODE, CITY_TABLE_KEY_PROVINCE_ID);

    private static final String CREATE_COUNTY = String.format(
            "create table %s(%s integer primary key autoincrement, %s text,%s text, %s integer)",
            COUNTY_TABLE_NAME, COUNTY_TABLE_PRIMARY_KEY, COUNTY_TABLE_KEY_NAME, COUNTY_TABLE_KEY_CODE, COUNTY_TABLE_KEY_CITY_ID);

    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

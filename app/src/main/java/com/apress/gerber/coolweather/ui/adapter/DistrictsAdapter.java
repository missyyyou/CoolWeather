package com.apress.gerber.coolweather.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apress.gerber.coolweather.R;
import com.apress.gerber.coolweather.model.bean.District;

import java.util.List;

/**
 * 作者：missyyyou on 2017/8/3 10:43.
 * 邮箱：yysha-94-03@foxmail.com
 */

public class DistrictsAdapter extends BaseAdapter {
    private Context mContext;
    private List<District> mDistricts;

    public DistrictsAdapter(Context context, List<District> districts) {
        mContext = context;
        mDistricts = districts;
    }

    @Override
    public int getCount() {
        return mDistricts.size();
    }

    @Override
    public Object getItem(int position) {
        return mDistricts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_district, parent, false);
        }
        TextView districtName = (TextView) convertView.findViewById(R.id.tv_district_name);
        districtName.setText(mDistricts.get(position).getDistrictName());
        return convertView;
    }
}

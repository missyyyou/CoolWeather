package com.apress.gerber.coolweather.util;

/**
 * Created by Administrator on 2017/3/14.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}

package com.apress.gerber.coolweather.util;

/**
 * 作者：missyyyou on 2017/3/14 07:55.
 * 邮箱：yysha-94-03@foxmail.com
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}

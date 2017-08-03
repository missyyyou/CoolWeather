package com.apress.gerber.coolweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 作者：missyyyou on 2017/3/15 07:56.
 * 邮箱：yysha-94-03@foxmail.com
 */
public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoUpdateReceiver.class);
        context.startService(i);
    }
}

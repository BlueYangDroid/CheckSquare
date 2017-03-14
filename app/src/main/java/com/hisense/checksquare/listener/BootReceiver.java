package com.hisense.checksquare.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hisense.checksquare.widget.log.LogUtil;
import com.hisense.checksquare.widget.log.XLogClient;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";
    public BootReceiver() {
    }

    /**
     不能静态注册的广播:
     android.intent.action.SCREEN_ON
     android.intent.action.SCREEN_OFF
     android.intent.action.BATTERY_CHANGED
     android.intent.action.CONFIGURATION_CHANGED
     android.intent.action.TIME_TICK
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        LogUtil.d(intent.getAction());
    }
}

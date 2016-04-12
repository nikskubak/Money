package com.money.recomandation;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public final class AlarmEventReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            handleAlarmEvent(context, intent.getAction());
        }
    }

    private void handleAlarmEvent(Context context, String action) {
        if (Config.ACTION_ALARM_EVENT.equals(action)) {

        }
    }

    public interface Config {
        String ACTION_ALARM_EVENT = "com.money.EVENT";
    }
}
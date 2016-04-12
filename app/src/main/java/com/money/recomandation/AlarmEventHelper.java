package com.money.recomandation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.text.format.DateUtils.HOUR_IN_MILLIS;

public final class AlarmEventHelper {
    private static final String TAG = AlarmEventReceiver.class.getSimpleName();

    private AlarmEventHelper() {
    }

    public static void startEventAt(Context context, int hour) {
        long currentTime = System.currentTimeMillis();
        getAlarmManager(context).setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                getTriggerTime(currentTime, hour),

                AlarmManager.INTERVAL_HOUR,
                buildAlarmEventReceiverPendingIntent(context)
        );
    }

    public static void stopEvent(Context context) {
        getAlarmManager(context).cancel(buildAlarmEventReceiverPendingIntent(context));
    }

    private static PendingIntent buildAlarmEventReceiverPendingIntent(Context context) {
        return PendingIntent.getBroadcast(context, 0, buildAlarmEventReceiverIntent(context), FLAG_UPDATE_CURRENT);
    }

    private static Intent buildAlarmEventReceiverIntent(Context context) {
        return new Intent(context, AlarmEventReceiver.class).setAction(AlarmEventReceiver.Config.ACTION_ALARM_EVENT);
    }

    private static AlarmManager getAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    private static long getStartTime(long currentTime, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    private static long getTriggerTime(long currentTime, int hour) {
        long startTime = getStartTime(currentTime, hour);
        return (currentTime > startTime) ? (startTime + HOUR_IN_MILLIS) : startTime;
    }
}
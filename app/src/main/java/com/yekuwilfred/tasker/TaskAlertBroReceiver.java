package com.yekuwilfred.tasker;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TaskAlertBroReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ALARM")
                .setContentTitle("Notify Reminder")
                .setContentText("This is just a reminder for alarm notification");

        Intent intentToCall = new Intent(context, TasksListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,intentToCall, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
    }
}

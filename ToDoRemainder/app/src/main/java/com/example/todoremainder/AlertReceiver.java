package com.example.todoremainder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    private static final String TAG ="AlertReceiver" ;

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper notificationHelper =new NotificationHelper(context);
        NotificationCompat.Builder builder = notificationHelper.getChannelNotification();
        builder.setContentText(intent.getStringExtra("title"));
        Log.i(TAG, "onReceive: "+intent.getStringExtra("title"));
        Log.i(TAG, "onReceive: "+intent.getStringExtra("id"));
        int id = intent.getStringExtra("id").hashCode();
        notificationHelper.getManager().notify(id,builder.build());
    }
}

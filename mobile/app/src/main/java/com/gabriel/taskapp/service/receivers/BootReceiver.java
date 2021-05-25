package com.gabriel.taskapp.service.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.gabriel.taskapp.service.services.AlarmService;


public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "ALO KRL", Toast.LENGTH_LONG).show();
        Intent serviceIntent = new Intent(context, AlarmService.class);
        context.startForegroundService(serviceIntent);
    }
}

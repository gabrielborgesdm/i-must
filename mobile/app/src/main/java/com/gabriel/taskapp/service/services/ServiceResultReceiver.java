package com.gabriel.taskapp.service.services;

import android.os.Bundle;
import android.os.Handler;

public class ServiceResultReceiver extends android.os.ResultReceiver {

    private Receiver mReceiver;

    public ServiceResultReceiver(Handler handler) {
        super(handler);
        // TODO Auto-generated constructor stub
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);

    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }

}
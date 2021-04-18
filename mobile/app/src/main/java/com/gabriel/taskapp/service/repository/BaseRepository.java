package com.gabriel.taskapp.service.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class BaseRepository {
    Context mContext;

    public BaseRepository(Context context) {
        this.mContext = context;
    }

    public boolean checkIsOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
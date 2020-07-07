package com.cq.daemon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * Service系统机制拉活
 */
public class StickyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //START_STICKY
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //只要 targetSdkVersion 不小于5，就默认是 START_STICKY。
        return super.onStartCommand(intent, flags, startId);
    }
}

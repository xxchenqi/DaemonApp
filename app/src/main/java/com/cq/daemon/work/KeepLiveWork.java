package com.cq.daemon.work;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


public class KeepLiveWork extends Worker {
    public static final String TAG = "KeepLiveWork";

    public KeepLiveWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e(TAG, "doWork: ");
        MyJobService.startJob(getApplicationContext());
        return Result.success();
    }
}

package com.cq.daemon.dual;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.cq.daemon.utils.Utils;

@SuppressLint("NewApi")
public class DualJobService extends JobService {
    public static final String TAG = "DualJobService";

    public static void startJob(Context context) {
        Log.e(TAG, "startJob");

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo.Builder builder = new JobInfo.Builder(8, new ComponentName(context.getPackageName(),
                DualJobService.class.getName())).setPersisted(true);

        // 小于7.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            builder.setPeriodic(1000);
        } else {
            builder.setMinimumLatency(1000);
        }

        jobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "onStartJob");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            startJob(this);
        }

        boolean isLocal = Utils.isRunningService(this, LocalService.class.getName());
        boolean isRemote = Utils.isRunningService(this, RemoteService.class.getName());

        if (!isLocal || !isRemote) {
            startService(new Intent(this, LocalService.class));
            startService(new Intent(this, RemoteService.class));
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}

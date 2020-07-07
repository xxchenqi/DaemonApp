package com.cq.daemon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.cq.daemon.account.AccountHelper;
import com.cq.daemon.activity.KeepManager;
import com.cq.daemon.dual.DualJobService;
import com.cq.daemon.dual.LocalService;
import com.cq.daemon.dual.RemoteService;
import com.cq.daemon.jobscheduler.MyJobService;
import com.cq.daemon.service.ForegroundService;
import com.cq.daemon.work.KeepLiveWork;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
//        KeepManager.getInstance().unregisterKeep(this);
        super.onDestroy();
    }

    public void keepActivity(View view) {
        //1像素保护
        KeepManager.getInstance().registerKeep(this);
    }

    public void foregroundService(View view) {
        // 前台服务保活
        startService(new Intent(this, ForegroundService.class));
    }

    public void stickyService(View view) {
        //sticky
        startService(new Intent(this, StickyService.class));
    }

    public void accountService(View view) {
        //账户拉活
        AccountHelper.addAccount(this);
        AccountHelper.autoSync();
    }

    public void jobService(View view) {
        //job拉活
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MyJobService.startJob(this);
        }

    }

    public void dualDaemon(View view) {
        //双进程+job守护
        startService(new Intent(this, LocalService.class));
        startService(new Intent(this, RemoteService.class));
        DualJobService.startJob(this);
    }

    public void workManager(View view) {
        //workManager拉活
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest
                .Builder(KeepLiveWork.class)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest);
    }
}

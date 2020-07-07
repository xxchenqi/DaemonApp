package com.cq.daemon.dual;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.cq.daemon.IMyAidlInterface;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class LocalService extends Service {
    public static final String TAG = "ProcessService";
    private ServiceConnection serviceConnection;
    private MyBinder myBinder;
    private static final int SERVICE_ID = 17;

    class MyBinder extends IMyAidlInterface.Stub {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myBinder = new MyBinder();
        serviceConnection = new MyServiceConnection();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {//4.3以下
            //将service设置成前台服务，并且不显示通知栏消息
            startForeground(SERVICE_ID, new Notification());
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { //Android4.3-->Android7.0
            //将service设置成前台服务
            startForeground(SERVICE_ID, new Notification());
            //删除通知栏消息
            startService(new Intent(this, InnerService.class));
        } else { // 8.0 及以上
            //通知栏消息需要设置channel
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //NotificationManager.IMPORTANCE_MIN 通知栏消息的重要级别  最低，不让弹出
            //IMPORTANCE_MIN 前台时，在阴影区能看到，后台时 阴影区不消失，增加显示 IMPORTANCE_NONE时 一样的提示
            //IMPORTANCE_NONE app在前台没有通知显示，后台时有
            NotificationChannel channel = new NotificationChannel("channel", "xx", NotificationManager.IMPORTANCE_NONE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                Notification notification = new NotificationCompat.Builder(this, "channel").build();
                //将service设置成前台服务，8.x退到后台会显示通知栏消息，9.0会立刻显示通知栏消息
                startForeground(SERVICE_ID, notification);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, RemoteService.class), serviceConnection, BIND_AUTO_CREATE);
        return super.onStartCommand(intent, flags, startId);
    }

    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "localService 可能被杀死了,拉活");
            startService(new Intent(LocalService.this, RemoteService.class));
            bindService(new Intent(LocalService.this, RemoteService.class),
                    serviceConnection, BIND_AUTO_CREATE);
        }
    }

    public static class InnerService extends Service {

        @Override
        public void onCreate() {
            super.onCreate();
            Log.e(TAG, "InnerService 服务创建了");
            // 让服务变成前台服务
            startForeground(SERVICE_ID, new Notification());
            // 关闭自己
            stopSelf();
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}

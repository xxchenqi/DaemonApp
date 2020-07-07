package com.cq.daemon.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

public class Utils {
    public static boolean isRunningService(Context context, String name) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo runningService : runningServices) {
            if (TextUtils.equals(runningService.service.getClassName(), name)) {
                return true;
            }
        }
        return false;
    }

}

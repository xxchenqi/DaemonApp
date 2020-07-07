package com.cq.daemon.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

/**
 * 1像素Activity
 */
public class KeepActivity extends Activity {

    private static final String TAG = "KeepActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "启动keep");
        Window window = getWindow();
        //放在左上角
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        //设置宽高
        params.width = 1;
        params.height = 1;
        //设置起始坐标
        params.x = 0;
        params.y = 0;
        window.setAttributes(params);
        // KeepActivity 创建一个弱引用
        KeepManager.getInstance().setKeep(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "关闭keep");
    }
}

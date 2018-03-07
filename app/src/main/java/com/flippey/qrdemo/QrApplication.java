package com.flippey.qrdemo;

import android.app.Application;

import com.lzy.okgo.OkGo;

/**
 * Created by flippey on 2018/3/7 17:17.
 */

public class QrApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.getInstance().init(this);
    }
}

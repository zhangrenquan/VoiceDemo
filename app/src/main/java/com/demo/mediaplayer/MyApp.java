package com.demo.mediaplayer;

import android.app.Application;
import android.content.Context;

import com.demo.mediaplayer.voice.sound.SoundUtil;

public class MyApp extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        SoundUtil.init();
    }
}

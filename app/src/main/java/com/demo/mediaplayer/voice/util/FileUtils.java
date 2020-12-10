package com.demo.mediaplayer.voice.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

/**
 * @author renquan
 * @describe 文件相关的工具类
 * @ideas
 */
public class FileUtils {


    /**
     * Assets获取资源
     *
     * @param context
     * @param filename
     * @return
     * @throws IOException
     */
    @NonNull
    public static AssetFileDescriptor getAssetFileDescription(@NonNull Context context, @NonNull String filename) throws IOException {
        AssetManager manager = context.getApplicationContext().getAssets();
        Log.d("sahdkahsdka","--------------"+filename);
        return manager.openFd(filename);
    }
}

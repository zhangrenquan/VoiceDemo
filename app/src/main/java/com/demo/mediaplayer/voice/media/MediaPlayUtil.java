package com.demo.mediaplayer.voice.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.mediaplayer.voice.util.FileUtils;
import com.demo.mediaplayer.voice.util.VoiceBuilder;
import com.demo.mediaplayer.voice.util.VoiceConstants;
import com.demo.mediaplayer.voice.util.VoiceTextTemplate;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author renquan
 * @describe 音频播放
 * MediaPlayer 方式播放音频
 * @ideas
 */

public class MediaPlayUtil {

    private ExecutorService mExecutorService;
    private Context mContext;

    private MediaPlayUtil(Context context) {
        this.mContext = context;
        this.mExecutorService = Executors.newCachedThreadPool();
    }

    @Nullable
    private volatile static MediaPlayUtil mVoicePlay = null;

    /**
     * 单例
     *
     * @return
     */
    @Nullable
    public static MediaPlayUtil with(Context context) {
        if (mVoicePlay == null) {
            synchronized (MediaPlayUtil.class) {
                if (mVoicePlay == null) {
                    mVoicePlay = new MediaPlayUtil(context);
                }
            }
        }
        return mVoicePlay;
    }

    /**
     * 默认收款成功样式
     *
     * @param money
     */
    public void play(String money) {
        play(money, false);
    }

    /**
     * 设置播报数字
     *
     * @param money
     * @param checkNum
     */
    public void play(String money, boolean checkNum) {
        VoiceBuilder voiceBuilder = new VoiceBuilder.Builder()
                .start(VoiceConstants.SUCCESS)
                .money(money)
                .unit(VoiceConstants.YUAN)
                .checkNum(checkNum)
                .builder();
        executeStart(voiceBuilder);
    }

    public void checkMoney(String money, boolean checkNum) {
        VoiceBuilder voiceBuilder = new VoiceBuilder.Builder()
                .start(VoiceConstants.PLEASEPAY)
                .money(money)
                .unit(VoiceConstants.YUAN)
                .checkNum(checkNum)
                .builder();
        executeStart(voiceBuilder);
    }


    /**
     * 接收自定义
     *
     * @param voiceBuilder
     */
    public void play(@NonNull VoiceBuilder voiceBuilder) {
        executeStart(voiceBuilder);
    }

    /**
     * 开启线程
     *
     * @param builder
     */
    private void executeStart(@NonNull VoiceBuilder builder) {
        final List<String> voicePlay = VoiceTextTemplate.genVoiceList(builder);
        if (voicePlay == null || voicePlay.isEmpty()) {
            return;
        }
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                start(voicePlay);
            }
        });
    }

    /**
     * 开始播报
     *
     * @param voicePlay
     */
    private void start(@NonNull final List<String> voicePlay) {
        synchronized (MediaPlayUtil.this) {
            final CountDownLatch mCountDownLatch = new CountDownLatch(1);
            AssetFileDescriptor assetFileDescription = null;

            try {
                final int[] counter = {0};
                assetFileDescription = FileUtils.getAssetFileDescription(mContext,
                        String.format(VoiceConstants.FILE_PATH, voicePlay.get(counter[0])));

                final MediaPlayer mMediaPlayer = new MediaPlayer();

                mMediaPlayer.setDataSource(
                        assetFileDescription.getFileDescriptor(),
                        assetFileDescription.getStartOffset(),
                        assetFileDescription.getLength());

                mMediaPlayer.prepareAsync();

                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mMediaPlayer.start();
                    }
                });
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.reset();
                        counter[0]++;

                        if (counter[0] < voicePlay.size()) {
                            try {
                                AssetFileDescriptor fileDescription2 = FileUtils.getAssetFileDescription(mContext,
                                        String.format(VoiceConstants.FILE_PATH, voicePlay.get(counter[0])));
                                mediaPlayer.setDataSource(
                                        fileDescription2.getFileDescriptor(),
                                        fileDescription2.getStartOffset(),
                                        fileDescription2.getLength());
                                mediaPlayer.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                                mCountDownLatch.countDown();
                            }
                        } else {
                            mediaPlayer.release();
                            mCountDownLatch.countDown();
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
                mCountDownLatch.countDown();
            } finally {
                if (assetFileDescription != null) {
                    try {
                        assetFileDescription.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                mCountDownLatch.await();
                notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

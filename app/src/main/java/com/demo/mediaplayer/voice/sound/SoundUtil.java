package com.demo.mediaplayer.voice.sound;

import android.content.Context;
import android.media.SoundPool;


import com.demo.mediaplayer.MyApp;
import com.demo.mediaplayer.voice.util.DateUtil;
import com.demo.mediaplayer.voice.util.VoiceBuilder;
import com.demo.mediaplayer.voice.util.VoiceConstants;
import com.demo.mediaplayer.voice.util.VoiceTextTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author renquan
 * SoundPool 方式播放
 */
public class SoundUtil {
    private ExecutorService mExecutorService;
    private static SoundUtil soundUtil;
    private final SoundPool soundPool;

    private String[] voice = {"1", "9", "dot"
            , "hundred", "hundred_million", "success", "ten", "ten_thousand", "thousand", "yuan"};

    private static HashMap<String, Integer> soundMap = new HashMap<>();

    private SoundUtil(Context context) {
        this.mExecutorService = Executors.newCachedThreadPool();
        //构建对象
        SoundPool.Builder spb = new SoundPool.Builder();
        spb.setMaxStreams(100);
        soundPool = spb.build();   //创建SoundPool对象
    }

    public static SoundUtil getInstance() {
        if (soundUtil == null) {
            init();
        }
        return soundUtil;
    }

    public static SoundUtil init() {
        if (soundUtil == null) {
            synchronized (SoundPool.class) {
                if (soundUtil == null) {
                    soundUtil = new SoundUtil(MyApp.context);
                    try {
                        for (int i = 0; i < soundUtil.voice.length; i++) {
                            String soundName = soundUtil.voice[i];
                            int soundId = soundUtil.soundPool.load(MyApp.context.getAssets().openFd(String.format(VoiceConstants.FILE_PATH, soundName)), 1);
                            soundMap.put(soundName, soundId);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return soundUtil;
    }

    //数字播报
    public void playNum(final String soundName) {
        if (soundName.equals("srzfje")) {
            long curTimeLong = DateUtil.getCurTimeLong();//当前时间
            Long upTimeLong = DateUtil.getDateUtil().getUpTimeLong();//上一次点击时间
            if (1700 > (curTimeLong - upTimeLong)) {
                return;
            }
            DateUtil.getDateUtil().setUpTimeLong(curTimeLong);
        }

        if (soundName.equals("csfkm")) {
            long curTimeLong = DateUtil.getCurTimeLong();//当前时间
            Long upTimeLong = DateUtil.getDateUtil().getUpTimeLong();//上一次点击时间
            if (1500 > (curTimeLong - upTimeLong)) {
                return;
            }
            DateUtil.getDateUtil().setUpTimeLong(curTimeLong);
        }

        if (soundName.equals("delete")) {
            long curTimeLong = DateUtil.getCurTimeLong();//当前时间
            Long upTimeLong = DateUtil.getDateUtil().getUpTimeLong();//上一次点击时间
            if (400 > (curTimeLong - upTimeLong)) {
                return;
            }
            DateUtil.getDateUtil().setUpTimeLong(curTimeLong);
        }

        if (soundName.equals("srhfje")) {
            long curTimeLong = DateUtil.getCurTimeLong();//当前时间
            Long upTimeLong = DateUtil.getDateUtil().getUpTimeLong();//上一次点击时间
            if (1500 > (curTimeLong - upTimeLong)) {
                return;
            }
            DateUtil.getDateUtil().setUpTimeLong(curTimeLong);
        }

        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (SoundUtil.class) {
                    try {
                        if (null == soundName || soundName.isEmpty()) {
                            return;
                        }
                        Integer soundId = soundMap.get(soundName);
                        if (null != soundId) {
                            soundPool.play(soundId, 1, 1, 1, 0, 1);
                            if (soundName.equals("srzfje") || soundName.equals("csfkm") || soundName.equals("srhfje")) {
                                Thread.sleep(1000);
                            }
                            if (soundName.length() == 1 || soundName.equals("dot")) {
                                Thread.sleep(300);
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    //组合播报
    public void play(final List<String> voicePlayer) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                if (voicePlayer.size() <= 0) {
                    return;
                }
                start(voicePlayer);
            }
        });
    }

    public void playMoney(final String start, final String money) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                VoiceBuilder voiceBuilder = new VoiceBuilder.Builder()
                        .start(start)
                        .money(money)
                        .unit(VoiceConstants.YUAN)
                        .checkNum(false)
                        .builder();
                List<String> voicePlay = VoiceTextTemplate.genVoiceList(voiceBuilder);

                start(voicePlay);
            }

        });
    }

    private void start(final List<String> voicePlayer) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    for (int i = 0; i < voicePlayer.size(); i++) {
                        String soundName = voicePlayer.get(i);
                        if (null == soundName || soundName.isEmpty()) {
                            continue;
                        }
                        Integer soundId = soundMap.get(soundName);
                        if (null != soundId) {
                            soundPool.play(soundId, 1, 1, 1, 0, 1);
                        }
                        try {
                            if (soundName.equals("success")) {
                                Thread.sleep(800);
                            } else if (soundName.equals("pleasepay")) {
                                Thread.sleep(800);
                            } else {
                                Thread.sleep(350);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }

}

package com.demo.mediaplayer;

import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.mediaplayer.voice.media.MediaPlayUtil;
import com.demo.mediaplayer.voice.sound.SoundUtil;
import com.demo.mediaplayer.voice.util.VoiceBuilder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mMedia1;
    private Button mSound1;
    private Button mMedia2;
    private Button mSound2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        mMedia1 = (Button) findViewById(R.id.media1);
        mMedia1.setOnClickListener(this);
        mSound1 = (Button) findViewById(R.id.sound1);
        mSound1.setOnClickListener(this);
        mMedia2 = (Button) findViewById(R.id.media2);
        mMedia2.setOnClickListener(this);
        mSound2 = (Button) findViewById(R.id.sound2);
        mSound2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.media1:
                // TODO 20/12/10
                VoiceBuilder voiceBuilder = (new VoiceBuilder.Builder()).start("1").builder();
                MediaPlayUtil.with(this).play(voiceBuilder);
                break;
            case R.id.sound1:
                // TODO 20/12/10
                SoundUtil.getInstance().playNum("1");
                break;
            case R.id.media2:
                // TODO 20/12/10
                MediaPlayUtil.with(this).play("99.99", false);
                break;
            case R.id.sound2:
                // TODO 20/12/10
                SoundUtil.getInstance().playMoney("success","99.99");
                break;
            default:
                break;
        }
    }
}
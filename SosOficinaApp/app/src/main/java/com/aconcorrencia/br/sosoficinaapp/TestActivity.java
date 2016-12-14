package com.aconcorrencia.br.sosoficinaapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.IOException;
import java.util.HashMap;

public class TestActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener {
    private SurfaceView mSurfaceView;
    private MediaPlayer mMediaPlayer;
    private MediaPlayer mPlayer;
    private SurfaceHolder mSurfaceHolder;
    private   Thread timer;
    boolean stop=false;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mSurfaceView = (SurfaceView)findViewById(R.id.surface);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(TestActivity.this);
        url="https://inducesmile.com/wp-content/uploads/2016/05/small.mp4";
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.relative_login).setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.FadeIn)
                                .duration(1000)
                                .playOn(findViewById(R.id.relative_login));
                    }
                });
            }
        };
        timer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Thread timer2 = new Thread() {
            public void run() {
                try {
                    sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mPlayer= MediaPlayer.create(TestActivity.this, R.raw.login_audio);
                mPlayer.setLooping(true);
                mPlayer.start();
            }
        };
        timer2.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDisplay(mSurfaceHolder);
        try {
            AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.car_graphic);
            if (afd == null) return;
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(TestActivity.this);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            timer = new Thread() {
                public void run() {
                    while(!stop) {
                        try {
                            sleep(8000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(mMediaPlayer!=null)
                        mMediaPlayer.seekTo(3000);

                    }
                }
            };
            timer.start();
        } catch (IOException e) {
            Log.w("ERRO","ERRO: "+e.toString());
            e.printStackTrace();
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }
    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mPlayer!=null){
            mPlayer.stop();
        }
        stop=true;
    }


    public void btnLogin(View view) {
        Intent it = new Intent(TestActivity.this,MainActivity.class);
        startActivity(it);
    }

}
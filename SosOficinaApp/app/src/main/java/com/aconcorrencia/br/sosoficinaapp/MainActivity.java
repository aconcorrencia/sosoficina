package com.aconcorrencia.br.sosoficinaapp;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import static com.aconcorrencia.br.sosoficinaapp.R.id.img;
import static com.aconcorrencia.br.sosoficinaapp.R.styleable.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void btnProximo(View v){
        Intent it = new Intent(MainActivity.this,ChoosePhotoActivity.class);
        startActivity(it);
    }


}

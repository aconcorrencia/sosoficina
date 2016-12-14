package com.aconcorrencia.br.sosoficinaapp;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class LoginActivity extends AppCompatActivity {
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        img = (ImageView) findViewById(R.id.img);
        img.setVisibility(View.INVISIBLE);
    }

    public void btnLogin(View view) {
        Intent it = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(it);
    }


    private void moveViewToScreenCenter(View view) {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.relativeMain);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();
        int originalPos[] = new int[2];
        view.getLocationOnScreen(originalPos);
        int yDest = (dm.heightPixels - convertDpToPixel(100)) - (view.getMeasuredHeight() / 2) - statusBarOffset;
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -(yDest - originalPos[1]));
        anim.setDuration(2500);
        anim.setFillAfter(true);
        view.startAnimation(anim);
    }

    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (hasFocus) {
            Thread timer = new Thread() {

                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.img).setVisibility(View.VISIBLE);
                            YoYo.with(Techniques.FadeIn)
                                    .duration(1000)
                                    .playOn(findViewById(R.id.img));
                            moveViewToScreenCenter(img);
                        }
                    });
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
        } else {
            //findViewById(R.id.relativeLayout2).setVisibility(View.INVISIBLE);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.relative_login).setVisibility(View.GONE);
        //findViewById(R.id.linear_main).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        findViewById(R.id.relative_login).setVisibility(View.GONE);
    }
}

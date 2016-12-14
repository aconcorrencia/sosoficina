package com.aconcorrencia.br.sosoficinaapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aconcorrencia.br.sosoficinaapp.sendphp.WrapData;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;

import static com.aconcorrencia.br.sosoficinaapp.ChoosePhotoActivity.QUALITY;

public class CarServiceActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    ImageView imgCar, lixeira;
    RelativeLayout rl;
    FloatingActionMenu fabMain;
    public static final int RESULT_OK = -1;
    private static final int IMG_CAM = 1;
    private static final int IMG_SDCARD = 2;
    public static final int QUALITY = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_car_service);
        rl = (RelativeLayout) findViewById(R.id.layout);
        imgCar = (ImageView) findViewById(R.id.car);
        lixeira = (ImageView) findViewById(R.id.lixeira);
        fabMain = (FloatingActionMenu) findViewById(R.id.fab);
        fabMain.showMenuButton(true);
        fabMain.setClosedOnTouchOutside(true);

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab_pintura);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab_recuperacao);


        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        ImageView img;
        LinearLayout.LayoutParams layoutParams;
        switch (view.getId()) {
            case R.id.fab_pintura:
                /*img = new ImageView(CarServiceActivity.this);
                img.setBackgroundResource(R.drawable.spray);
                img.setOnTouchListener(CarServiceActivity.this);
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                img.setLayoutParams(layoutParams);
                rl.addView(img);
                fabMain.close(true);*/
                File file = new File(android.os.Environment.getExternalStorageDirectory(), "img.png");
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, IMG_CAM);
                break;
            case R.id.fab_recuperacao:
                img = new ImageView(CarServiceActivity.this);
                img.setBackgroundResource(R.drawable.martelo);
                img.setOnTouchListener(CarServiceActivity.this);
                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                img.setLayoutParams(layoutParams);
                rl.addView(img);
                fabMain.close(true);
                break;

        }
    }

    private int _xDelta;
    private int _yDelta;
    boolean delete = false;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                if (delete) {
                    MediaPlayer mPlayer = MediaPlayer.create(CarServiceActivity.this, R.raw.trash_sound);
                    mPlayer.start();
                    view.setVisibility(View.GONE);
                    lixeira.setImageResource(R.drawable.trash_close);
                    delete = false;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                int _x;
                int _y;
                _x = X - _xDelta;
                if (Y < imgCar.getTop()) {
                    _y = imgCar.getTop();
                } else {
                    if (Y > imgCar.getBottom()) {
                        _y = imgCar.getBottom();
                    } else {
                        _y = Y - _yDelta;
                    }
                }
                layoutParams.leftMargin = _x;
                layoutParams.topMargin = _y;
                layoutParams.rightMargin = 250 * -1;
                layoutParams.bottomMargin = 250 * -1;
                if (_y > lixeira.getTop() && _y < lixeira.getBottom()) {
                    if (_x < lixeira.getRight() && _x > lixeira.getLeft()){
                        lixeira.setImageResource(R.drawable.trash_open);
                        delete = true;
                    }else {
                        lixeira.setImageResource(R.drawable.trash_close);
                        delete = false;

                    }
                } else {
                    lixeira.setImageResource(R.drawable.trash_close);
                    delete = false;

                }
                view.setLayoutParams(layoutParams);
                break;
        }
        return true;
    }

    public  void btnLixeira(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Arraste e solte aqui o componente se desejar exclui-lo!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        File file = null;
        WrapData wd = new WrapData();
        if (data != null && requestCode == IMG_SDCARD && resultCode == RESULT_OK) {
            Uri img = data.getData();
            String[] cols = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(img, cols, null, null, null);
            cursor.moveToFirst();

            int indexCol = cursor.getColumnIndex(cols[0]);
            String imgString = cursor.getString(indexCol);
            cursor.close();
            Log.w("1", "1");
            file = new File(imgString);
            Log.w("3", "3");
            if (file != null) {
                wd.getImage().getProportionalBitmap(file, QUALITY, "X");
                wd.getImage().setMimeFromImgPath(file.getPath());

            }
        } else if (requestCode == IMG_CAM && resultCode == RESULT_OK) {
            file = new File(android.os.Environment.getExternalStorageDirectory(), "img.png");
            if (file != null) {
                wd.getImage().getProportionalBitmap(file, QUALITY, "X");
                wd.getImage().setMimeFromImgPath(file.getPath());
            }
        }
        if (wd.getImage().getBitmap() != null) {
           imgCar.setImageBitmap(wd.getImage().getBitmap());
        }
    }
}

package com.aconcorrencia.br.sosoficinaapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.aconcorrencia.br.sosoficinaapp.sendphp.WrapData;

import java.io.File;

public class ChoosePhotoActivity extends AppCompatActivity {

    public static final int RESULT_OK = -1;
    private static final int IMG_CAM = 1;
    private static final int IMG_SDCARD = 2;
    public static final int QUALITY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo);
    }

    public void btnCam(View view){
        File file = new File(android.os.Environment.getExternalStorageDirectory(), "img.png");
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, IMG_CAM);
    }

    public void btnImage(View view){
        Intent it = new Intent(ChoosePhotoActivity.this,CarServiceActivity .class);
        startActivity(it);
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

        }

    }
}

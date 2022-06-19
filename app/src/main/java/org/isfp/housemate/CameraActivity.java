package org.isfp.housemate;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nullable;

public class CameraActivity extends AppCompatActivity {
    final private static String TAG = "태그명";
    Button btcamera1;
    ImageView pic;
    private Boolean isPermission = true;
    private static final int PICK_FROM_CAMERA = 2;
    final static int TAKE_PICTURE = 1;

    // 경로 변수와 요청변수 생성
    String mCurrentPhotoPath;
    final static int REQUEST_TAKE_PHOTO = 1;

    //private static final int REQUEST_IMAGE_CAPTURE = 672;
    //private static final int REQUEST_IMAGE_CODE = 101;
    private String imageFilePath;
    private Uri photoUri;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkwork);
        pic = findViewById(R.id.pic);
        btcamera1 = findViewById(R.id.btcamera1);
        File sdcard = Environment.getExternalStorageDirectory();
        String imageFileName = "capture.jpg";
        file = new File(sdcard, imageFileName);


        pic = findViewById(R.id.pic);

        btcamera1 = findViewById(R.id.btcamera1);
        btcamera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture();
            }


        });
    }
        public void capture () {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            //startActivityForResult(i, 101);
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                Log.d(TAG, "권한 설정 완료");
//            } else {
//                Log.d(TAG, "권한 설정 요청");
//                ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//            }
//        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == 101  && resultCode == Activity.RESULT_OK){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                pic.setImageBitmap(bitmap);
            }
        }

    }


    //    public void takePic(){
//        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(i.resolveActivity(getPackageManager())!=null){
//            startActivityForResult(i,REQUEST_IMAGE_CODE);
//        }
//    }




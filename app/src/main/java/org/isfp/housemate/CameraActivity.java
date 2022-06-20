package org.isfp.housemate;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.media.ExifInterface;
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
import androidx.core.content.ContextCompat;
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

    private String imageFilePath;//경로변수
    private Uri photoUri;

    final static int REQUEST_TAKE_PHOTO = 1;//요청변수
    final static int TAKE_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkwork);
        pic = findViewById(R.id.pic);
        btcamera1 = findViewById(R.id.btcamera1);
        btcamera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cap();
            }
        });
    }

    public void cap() {
        int permissionCheck = ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
        } else {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photofile = null;
            try {
                photofile = createImageFile();
            } catch (IOException ex) {
            }
            if (photofile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photofile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            }
            startActivityForResult(i, REQUEST_TAKE_PHOTO);//액티비티로 다시 REQUEST보내기
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        pic.setImageURI(photoUri);
    }
    public String getRealPathFromURI(Uri contentUri) {

        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        photoUri= photoUri.fromFile(new File(path));

        cursor.close();
        return path;
    }

    private File createImageFile() throws IOException {
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String FileName = "JPEG_" + time + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                FileName,
                ".jpg",
                storageDir
        );
        return image;
    }
}









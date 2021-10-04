package com.poulstar.steganography;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EncodeActivity extends Activity {

    ImageView imgImage;
    Button btnSave, btnShare, btnCapture;
    EditText txtText;
    Bitmap capturedImage;

    private final int REQUEST_ID = 1;
    private final int IMAGE_CAPTURE_ID = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encode_layout);

        imgImage = findViewById(R.id.imgView);
        txtText = findViewById(R.id.txtText);
        btnSave = findViewById(R.id.btnSave);
        btnShare = findViewById(R.id.btnShare);
        btnCapture = findViewById(R.id.btnCapture);

        btnCapture.setOnClickListener(view -> {
            capture();
        });
        btnShare.setOnClickListener(view -> {
            share();
        });
        btnSave.setOnClickListener(view -> {
            save();
        });

        checkPermissions();
    }

    private void capture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_ID);
    }

    private void share() {
//        if(capturedImage != null) {
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setType("image/jpeg");
//
//        }
    }

    private void save() {
        if(capturedImage != null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "stegano.png");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            try {
                OutputStream out = getContentResolver().openOutputStream(uri);
                capturedImage.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();
                Toast.makeText(getApplicationContext(), "Saved image to "+ uri.getPath(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_CAPTURE_ID) {
            if(resultCode == RESULT_OK) {
                capturedImage = (Bitmap) data.getExtras().get("data");
                imgImage.setImageBitmap(capturedImage);
            }
        }
    }

    private void checkPermissions() {
        if(Build.VERSION.SDK_INT >= 26) {
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_ID);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_ID) {
            boolean result = true;
            for (int gResult : grantResults) {
                if(gResult != PackageManager.PERMISSION_GRANTED) {
                    result = false;
                }
            }
            if(!result) {
                Intent intent = new Intent(EncodeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}

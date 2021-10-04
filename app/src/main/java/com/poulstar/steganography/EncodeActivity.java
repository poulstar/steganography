package com.poulstar.steganography;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EncodeActivity extends Activity {

    ImageView imgImage;
    Button btnSave, btnShare, btnCapture;
    EditText txtText;

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

    }

    private void save() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_CAPTURE_ID) {
            if(resultCode == RESULT_OK) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                imgImage.setImageBitmap(imageBitmap);
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

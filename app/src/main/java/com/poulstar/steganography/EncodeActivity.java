package com.poulstar.steganography;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EncodeActivity extends Activity {

    ImageView imgImage;
    Button btnSave, btnShare, btnCapture;
    EditText txtText;

    private final int REQUEST_ID = 5;

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

    }

    private void share() {

    }

    private void save() {

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

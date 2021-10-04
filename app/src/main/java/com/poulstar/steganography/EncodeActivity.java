package com.poulstar.steganography;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class EncodeActivity extends Activity {

    ImageView imgImage;
    Button btnSave, btnShare, btnCapture;
    EditText txtText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encode_layout);

        imgImage = findViewById(R.id.imgView);
        txtText = findViewById(R.id.txtText);
        btnSave = findViewById(R.id.btnSave);
        btnShare = findViewById(R.id.btnShare);
        btnShare = findViewById(R.id.btnCapture);
    }


}

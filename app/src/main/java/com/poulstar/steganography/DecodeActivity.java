package com.poulstar.steganography;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class DecodeActivity extends Activity {


    ImageView imgPreview;
    Button btnLoad;
    TextView txtMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decode_layout);

        imgPreview = findViewById(R.id.imgPreview);
        btnLoad = findViewById(R.id.btnLoad);
        txtMessage = findViewById(R.id.txtMessage);
    }
}

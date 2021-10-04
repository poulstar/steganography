package com.poulstar.steganography;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {

    Button btnDecode, btnEncode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        btnEncode = findViewById(R.id.btnEncode);
        btnDecode = findViewById(R.id.btnDecode);

        btnEncode.setOnClickListener(view -> {
            openEncode();
        });
        btnDecode.setOnClickListener(view -> {
            openDecode();
        });
    }

    private void openEncode() {
        Intent intent = new Intent(MainActivity.this, EncodeActivity.class);
        startActivity(intent);
    }

    private void openDecode() {
        Intent intent = new Intent(MainActivity.this, DecodeActivity.class);
        startActivity(intent);
    }
}

package com.poulstar.steganography;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class DecodeActivity extends Activity {


    ImageView imgPreview;
    Button btnLoad;
    TextView txtMessage;
    Bitmap image;

    private final int MEDIA_REQUEST_ID = 8;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decode_layout);

        imgPreview = findViewById(R.id.imgPreview);
        btnLoad = findViewById(R.id.btnLoad);
        txtMessage = findViewById(R.id.txtMessage);

        btnLoad.setOnClickListener(v -> {
            loadImage();
        });
    }

    private void loadImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/png");
        startActivityForResult(intent, MEDIA_REQUEST_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MEDIA_REQUEST_ID) {
            if(resultCode == RESULT_OK) {
                Uri uri = data.getData();
                try {
                    InputStream input = getContentResolver().openInputStream(uri);
                    image = BitmapFactory.decodeStream(input);

                    runOnUiThread(() -> {
                        imgPreview.setImageBitmap(image);
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

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

    private final String TAG = "DecoderActivity";
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
        startActivityForResult(Intent.createChooser(intent, "Save file"), MEDIA_REQUEST_ID);
    }

    public void decodeMessage() {
        if(image != null) {
            String message = "";
            for (int j=0; j < image.getHeight(); j++) {
                for (int i=0; i < image.getWidth(); i++) {
                    int color = image.getPixel(i, j);
                    char alpha = (char) (Color.alpha(color) - 120);
                    Log.i(TAG, String.format("i(%d),j(%d): %s", i, j, alpha));
                    if(alpha == '$') {
                        final String finalMessage = message;
                        runOnUiThread(() -> {
                            txtMessage.setText(finalMessage);
                        });
                        return;
                    }else {
                        message += alpha;
                    }
                }
            }
        }
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
                    decodeMessage();

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

package com.poulstar.steganography;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

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
    String fullSizeImagePath;

    private final String TAG = "EncoderActivity";
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

        // Save full size image
        try {
            File file = File.createTempFile(
                    "stegano_image",
                    ".png",
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    file);
            fullSizeImagePath = file.getAbsolutePath();
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(cameraIntent, IMAGE_CAPTURE_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void share() {
        if(capturedImage != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            Uri uri = save();
            if(uri != null) {
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Share image"));
            }
        }
    }

    private Uri save() {
        if(capturedImage != null) {
            this.appendMessage();
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "stegano");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            try {
                OutputStream out = getContentResolver().openOutputStream(uri);
                capturedImage.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();
                Toast.makeText(getApplicationContext(), "Saved image to "+ uri.getPath(), Toast.LENGTH_LONG).show();
                return uri;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void appendMessage() {
        if(capturedImage != null) {
            String message = txtText.getText().toString() + "$";
            int counter = 0;
            for (char c : message.toCharArray()) {
                int j = counter / capturedImage.getWidth();
                int i = counter % capturedImage.getWidth();
                int color = capturedImage.getPixel(i, j);
                capturedImage.setPixel(i, j,
                        Color.argb(c + 120, Color.red(color), Color.green(color), Color.blue(color)));
                Log.i(TAG, String.format("i(%d),j(%d): %s", i, j, c));
                counter++;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_CAPTURE_ID) {
            if(resultCode == RESULT_OK) {
//                Bitmap image = (Bitmap) data.getExtras().get("data");

                Bitmap originalImage = BitmapFactory.decodeFile(fullSizeImagePath);
                capturedImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
                capturedImage.setHasAlpha(true);
                imgImage.setImageBitmap(originalImage);
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

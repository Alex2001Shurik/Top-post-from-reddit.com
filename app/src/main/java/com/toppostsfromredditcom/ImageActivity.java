package com.toppostsfromredditcom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;

public class ImageActivity extends AppCompatActivity {

    FileOutputStream outputStream;

    ImageView imageView;


    Button save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

        }

        imageView = findViewById(R.id.url);
        save = findViewById(R.id.btn_save);

        Bundle bundle = this.getIntent().getExtras();
        String url = bundle.getString("url");

        Glide.with(this).load(url).into(imageView);

        save.setOnClickListener(view -> {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            File file = Environment.getExternalStorageDirectory();
            File dir = new File(file + "/Download/");
            File outFIle = new File(dir, System.currentTimeMillis() + ".jpg");
            try{
                outputStream = new FileOutputStream(outFIle);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            Toast.makeText(this, "Image is saved", Toast.LENGTH_SHORT).show();
        });
    }
}

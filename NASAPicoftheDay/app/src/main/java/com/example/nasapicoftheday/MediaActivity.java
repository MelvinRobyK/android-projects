package com.example.nasapicoftheday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;

public class MediaActivity extends AppCompatActivity {

    private ImageView imageView;
    private WebView webView;
    private ConstraintLayout constraintLayout;
    private Bitmap bitmap;
    private String media_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        imageView = findViewById(R.id.image_view);
        webView = findViewById(R.id.web_view);
        constraintLayout = findViewById(R.id.constraint_layout2);

        Intent intent = getIntent();
        media_url = intent.getStringExtra("url");
        Log.i("info", "Inside MediaActivity : "+media_url);
        if (intent.getStringExtra("media_type").equals("image")) {
            constraintLayout.setBackgroundColor(Color.BLACK);
            byte[] bytes=intent.getByteArrayExtra("bitmap")  ;
            bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);

        } else {
            constraintLayout.setBackgroundColor(Color.BLACK);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());
            webView.loadUrl(intent.getStringExtra("url"));
            webView.setVisibility(View.VISIBLE);
        }
    }
}
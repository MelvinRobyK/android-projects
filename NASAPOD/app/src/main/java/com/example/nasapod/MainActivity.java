package com.example.nasapod;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView title,explanation;
    String api_url="https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";
    String api_url2="https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&date=YYYY-MM-DD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title=findViewById(R.id.title);
        explanation=findViewById(R.id.description);
        new hitNASA(title,explanation).execute();
    }
}
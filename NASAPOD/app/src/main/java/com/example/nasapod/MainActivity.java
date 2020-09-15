package com.example.nasapod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView titleView, explanationView;
    ConstraintLayout constraintLayout;
    ImageView imageView;
    VideoView videoView;
    String api_url = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";
    String date = null;

    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleView = (TextView) findViewById(R.id.title);
        explanationView = findViewById(R.id.description);
        constraintLayout = findViewById(R.id.constraint_layout);
        imageView = findViewById(R.id.srcImage);
        videoView = findViewById(R.id.srcVideo);
        new hitNasaApi().execute();
    }

    public void getDate(View view) {
        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                date = i + "-" + (i1 + 1) + "-" + i2;
                videoView.setVisibility(View.INVISIBLE);
                api_url = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&date=" + date;
                new hitNasaApi().execute();
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private class hitNasaApi extends AsyncTask<Void, Void, String> {

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        String mediaUrl;
        String mediaType;
        Bitmap bitmap = null;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(api_url);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null)
                        stringBuilder.append(line).append("\n");
                    bufferedReader.close();

                    JSONObject reader = new JSONObject(stringBuilder.toString());
                    mediaUrl = reader.getString("url");
                    mediaType = reader.getString("media_type");
                    if (mediaType.equals("image")) {
                        url = new URL(mediaUrl);
                       /* HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
                        mUrlConnection.connect();
                        InputStream inputStream = mUrlConnection.getInputStream();
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        bitmap = BitmapFactory.decodeStream(bufferedInputStream);*/
                        InputStream in = new java.net.URL(mediaUrl).openStream();
                        bitmap=BitmapFactory.decodeStream(in);
                    }
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            String title = null;
            String explanation = null;
            try {
                JSONObject reader = new JSONObject(s);
                title = reader.getString("title");
                explanation = reader.getString("explanation");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            titleView.setText(title);
            explanationView.setText(explanation);
            if (mediaType.equals("image")) {
                // i1.get().setImageBitmap(bitmap);
                BitmapDrawable drawable = new BitmapDrawable(bitmap);
                constraintLayout.setBackgroundDrawable(drawable);
            } else {
                Uri uri = Uri.parse(mediaUrl);
                constraintLayout.setBackgroundColor(Color.BLACK);
                videoView.setVideoURI(uri);
                videoView.start();
            }
        }
    }

}
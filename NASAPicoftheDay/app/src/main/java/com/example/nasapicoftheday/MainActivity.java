package com.example.nasapicoftheday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView titleTextView,descriptionTextView;
    private ConstraintLayout constraintLayout;
    private String nasa_api_url = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";
    private String title,description,mediaType,mediaUrl,videoUrl;
    private Bitmap bitmapImage;
    private RequestQueue mQueue;
    private ProgressDialog progressDialog;
    private ImageButton playButton;

    private int mYear, mMonth, mDay;
    final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleTextView = findViewById(R.id.title);
        descriptionTextView = findViewById(R.id.description);
        constraintLayout = findViewById(R.id.constraint_layout);
        playButton=findViewById(R.id.play_Button);

        mQueue = VolleySingleton.getInstance(this).getRequestQueue();

        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        HitNasaApi(nasa_api_url);
    }

    private void HitNasaApi(String nasa_api_url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, nasa_api_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            title = response.getString("title");
                            description = response.getString("explanation");
                            mediaType = response.getString("media_type");
                            mediaUrl = response.getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mediaUrl = ResolveUrl();  //Resolve the background image i.e. thumbnail if it is a video
                        if(mediaUrl != null)
                            SetImage();
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

            }
        });

        mQueue.add(request);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }

    private String ResolveUrl() {
        if(mediaType.equals("image")) {
            playButton.setImageResource(R.drawable.zoom2);
            return mediaUrl;
        }
        else {  //its a video
            videoUrl=mediaUrl;
            String videoId = null;
            playButton.setImageResource(R.drawable.play2);
            if(mediaUrl.contains("youtube")) {
                if(mediaUrl.contains("?rel"))
                    videoId = StringUtils.substringBetween(mediaUrl,"embed/","?rel");
                else
                    videoId = StringUtils.substringAfter(mediaUrl,"embed/");
                return "https://img.youtube.com/vi/"+videoId+"/hqdefault.jpg";
            }
            else if(mediaUrl.contains("vimeo")) {
                videoId = StringUtils.substringAfter(mediaUrl,"video/");
                String vimeoApi = "https://vimeo.com/api/oembed.json?url=https://player.vimeo.com/video/"+videoId;  //this is the vimeo api for getting the video metadata
                JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET,vimeoApi,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    mediaUrl = response.getString("thumbnail_url");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                SetImage();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                mQueue.add(request2);
                return null;
            }
            return null;
        }
    }

    private void SetImage(){
            Picasso.get().load(mediaUrl).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    bitmapImage = bitmap;
                    BitmapDrawable drawable = new BitmapDrawable(bitmap);
                    constraintLayout.setBackground(drawable);
                    titleTextView.setText(title);
                    descriptionTextView.setText(description);
                    playButton.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }

    public void GetDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                mYear=i;
                mMonth=i1;
                mDay=i2;
                String date = i + "-" + (i1 + 1) + "-" + i2;
                String api_url = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&date=" + date;
                HitNasaApi(api_url);
                playButton.setVisibility(View.INVISIBLE);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    public void ShowFull(View view) {
        Intent intent=new Intent(this,MediaActivity.class);
        if(mediaType.equals("image")){
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.JPEG,75,stream);
            byte[] bytes=stream.toByteArray();
            intent.putExtra("bitmap",bytes);
        }
        intent.putExtra("url",videoUrl);
        intent.putExtra("media_type",mediaType);
        startActivity(intent);

    }
}
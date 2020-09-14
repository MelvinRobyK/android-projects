package com.example.nasapod;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class hitNASA extends AsyncTask<Void,Void,String> {

    private WeakReference<TextView> titleView;
    private WeakReference<TextView> explanationView;
    String api_url="https://api.nasa.gov/planetary/apod?api_key="+R.string.nasa_api_key;

    hitNASA(TextView textView1,TextView textView2){
        titleView=new WeakReference<>(textView1);
        explanationView=new WeakReference<>(textView2);
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
                return stringBuilder.toString();
            }
            finally {
                urlConnection.disconnect();
            }
        }
        catch (Exception e){
            Log.e("ERROR",e.getMessage(),e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        String title=null;
        String explanation=null;
        String imgUrl=null;
        try {
            JSONObject reader=new JSONObject(s);
            title=reader.getString("title");
            explanation=reader.getString("explanation");
            imgUrl=reader.getString("hdurl");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        titleView.get().setText(title);
        explanationView.get().setText(explanation);
    }
}

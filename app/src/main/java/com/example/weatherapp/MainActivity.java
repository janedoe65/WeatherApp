package com.example.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText cityEditText;
    TextView ausgabeTextView;



    public void getWeather(View view){

        try{
            DownloadTask task = new DownloadTask();
            String cityName = URLEncoder.encode(cityEditText.getText().toString(), "UTF-8");

            task.execute("http://api.apixu.com/v1/current.json?key=bf41079f1ce149808cf112153181701&q=" + cityName);
            ausgabeTextView.setVisibility(View.VISIBLE);
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(cityEditText.getWindowToken(), 0);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = findViewById(R.id.cityEditText);
        ausgabeTextView = findViewById(R.id.ausgabeTextView);

    }


    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();


            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try{
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("current");
                Log.i("WeatherINFO", weatherInfo);

                JSONObject currentJSONObject = new JSONObject(weatherInfo);
                String temperatureInC = currentJSONObject.getString("temp_c") + "°C";

                if(!temperatureInC.equals("")){
                    ausgabeTextView.setText(temperatureInC);
                }else{
                    Toast.makeText(MainActivity.this, "Temperatur konnte nicht gefunden werden", Toast.LENGTH_SHORT).show();
                }


            }catch (Exception e){
                e.printStackTrace();
                ausgabeTextView.setVisibility(View.INVISIBLE);
            }


        }

    }

}


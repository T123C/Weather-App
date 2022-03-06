package com.tc.weatherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //assigning XML parameters in Java
    EditText cityName;
    Button findButton;
    TextView mainTemp;
    TextView mainCondition;
    TextView minTemp;
    TextView maxTemp;
    TextView sunrise;
    TextView sunset;
    TextView windSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //attaching XML IDs to Java fields
        cityName = findViewById(R.id.city_name);
        findButton = findViewById(R.id.find_button);
        mainTemp = findViewById(R.id.main_temp);
        mainCondition = findViewById(R.id.main_condition);
        minTemp = findViewById(R.id.min_temp);
        maxTemp = findViewById(R.id.max_temp);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        windSpeed = findViewById(R.id.wind);

        //'FIND' button click method
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResponse();
            }
        });
    }

    //method to extract API data using Volley library and assign values to Java fields
    public void getResponse() {

        // Instantiate the RequestQueue with Volley library
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        //URL used with custom city name and custom API key
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + this.cityName.getText().toString() + "&units=metric&appid=b60e6c75bd6debf936d8b507829ea186";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //creating a JSON object to gather the data from API using try-catch
                        JSONObject myJsonObject;

                        try {
                            //assigning JSON object to the API response and using it to find specific values in the response
                            myJsonObject = new JSONObject(response);
                            JSONObject weather = myJsonObject.getJSONArray("weather").getJSONObject(0);
                            JSONObject main = myJsonObject.getJSONObject("main");
                            JSONObject sys = myJsonObject.getJSONObject("sys");
                            JSONObject wind = myJsonObject.getJSONObject("wind");

                            //adding extra fields to format the time number string from API to get the time in hrs and mins
                            long sun = sys.getLong("sunrise");
                            String formatSun = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sun * 1000));

                            long set = sys.getLong("sunset");
                            String formatSet = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(set * 1000));

                            //setting the acquired values to created Java fields for XML values
                            mainCondition.setText(weather.getString("description").toUpperCase());
                            mainTemp.setText(main.getInt("temp") + "°C");
                            minTemp.setText("Min Temp: " + (main.getInt("temp_min")) + "°C");
                            maxTemp.setText("Max Temp: " + (main.getInt("temp_max")) + "°C");
                            windSpeed.setText(wind.getString("speed") + " m/s");
                            sunrise.setText(formatSun);
                            sunset.setText(formatSet);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
            }
        });
        // Adding the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
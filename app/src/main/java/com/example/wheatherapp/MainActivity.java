package com.example.wheatherapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import retrofit2.Call;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
SearchView searchView;
TextView resultTextView;
Api_call apiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

LinearLayout linearLayout = findViewById(R.id.main);
        searchView = findViewById(R.id.serach);
        resultTextView = findViewById(R.id.textView);

        // Set up Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiCall = retrofit.create(Api_call.class);

        // Set up SearchView listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchWeatherData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optional: Handle text change
                return false;
            }
        });
    }

    // Fetch weather data using Retrofit
    private void fetchWeatherData(String city) {
        String apiKey = "a4988bdf68ba20c14a4dd36f38dd498d"; // Replace with your OpenWeatherMap API key

        Call<Root> call = apiCall.getWeather(city, apiKey);
        call.enqueue(new Callback<Root>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Root> call, @NonNull Response<Root> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Root weatherData = response.body();
                    double temp = weatherData.main.temp - 273.15;

                    String result = "City: " + weatherData.name + "\n" +
                            "Temperature: " + temp + "°C\n" +
                            "Weather: " + weatherData.weather.get(0).description
                            +"feels_like"+
                            "\n"+weatherData.main.feels_like;
                    resultTextView.setText(result);
                } else {
                    resultTextView.setText("City not found. Please try again.");
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(@NonNull Call<Root> call, @NonNull Throwable t) {
                resultTextView.setText("Error: " + t.getMessage());
            }
        });
}
}















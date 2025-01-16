package com.example.wheatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
SearchView searchView;
TextView  loctaions, temps,feels, high_temp, low_temp,sunrises,sunsets;
Api_call apiCall;
SharedPreferences sharedPreferences;
    LinearLayout linearLayout;
    @SuppressLint("MissingInflatedId")
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
        sharedPreferences = getSharedPreferences("WeatherAppPrefs", Context.MODE_PRIVATE);

        temps = findViewById(R.id.temps);
 linearLayout = findViewById(R.id.main);
 searchView = findViewById(R.id.search);
 feels =findViewById(R.id.feeling);
 low_temp =findViewById(R.id.low_temp);
 high_temp = findViewById(R.id.high_temp);
 sunrises = findViewById(R.id.sunrise);
 sunsets = findViewById(R.id.sunset);
 loctaions =findViewById(R.id.location);


        // Set up Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiCall = retrofit.create(Api_call.class);
        String username = sharedPreferences.getString("location", "defaultName");
        if (username.equals("defaultName")) {
            fetchWeatherData("dombivli");
        } else {
            fetchWeatherData(username);
        }
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
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onResponse(@NonNull Call<Root> call, @NonNull Response<Root> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Root weatherData = response.body();
                    double temp = weatherData.main.temp - 273.15;
                    temps.setText("Current temperature" + "\n" + String.format("%.2f°C", temp));
                    double low_temps = weatherData.main.temp_min -273.15;
                    low_temp.setText("Today's Low Temperature" + "\n" + String.format("%.2f°C", low_temps));
                    double high_temps = weatherData.main.temp_max -273.15;
                    high_temp.setText("Today's High Temperature" + "\n" + String.format("%.2f°C", high_temps));
                    double feelings = weatherData.main.feels_like-273.15;
                    feels.setText("Feeling Temperature" + "\n" + String.format("%.2f°C", feelings));
                    String location = weatherData.name;
                    loctaions.setText(location);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("location", city);
                    editor.apply();

                    long sunriseTime = weatherData.sys.sunrise * 1000L;  // Convert to milliseconds
                    long sunsetTime = weatherData.sys.sunset * 1000L;

                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    Date sunriseDate = new Date(sunriseTime);
                    Date sunsetDate = new Date(sunsetTime);
                    sunrises.setText("Sunrise: " + sdf.format(sunriseDate));
                    sunsets.setText("Sunset: " + sdf.format(sunsetDate));

                    // Convert to milliseconds
                    String weatherMain = weatherData.weather.get(0).main;
                    if ("Clear".equals(weatherMain)) {
                        linearLayout.setBackgroundResource(R.drawable.clear_sky); // Path to clear sky image
                    } else if ("Clouds".equals(weatherMain)) {
                        linearLayout.setBackgroundResource(R.drawable.colud_background); // Path to cloudy image
                    } else if ("Rain".equals(weatherMain)) {
                        linearLayout.setBackgroundResource(R.drawable.rain_background); // Path to rain image
                    } else if ("Drizzle".equals(weatherMain)) {
                        linearLayout.setBackgroundResource(R.drawable.drizzle); // Path to drizzle image
                    } else if ("Thunderstorm".equals(weatherMain)) {
                        linearLayout.setBackgroundResource(R.drawable.thunderstorm); // Path to thunderstorm image
                    } else if ("Snow".equals(weatherMain)) {
                        linearLayout.setBackgroundResource(R.drawable.snow_background); // Path to snow image
                    } else if ("Mist".equals(weatherMain)) {
                        linearLayout.setBackgroundResource(R.drawable.mist); // Path to mist image
                    } else if ("Haze".equals(weatherMain)) {
                        linearLayout.setBackgroundResource(R.drawable.haze); // Path to haze image
                    } else if ("Fog".equals(weatherMain)) {
                        linearLayout.setBackgroundResource(R.drawable.fog); // Path to fog image
                    } else if ("Smoke".equals(weatherMain)) {
                        linearLayout.setBackgroundResource(R.drawable.smoke); // Path to smoke image
                    } else if ("Dust".equals(weatherMain)) {
                        linearLayout.setBackgroundResource(R.drawable.dust); // Path to dust image
                    } else if ("Sand".equals(weatherMain)) {
                        linearLayout.setBackgroundResource(R.drawable.sand); // Path to sand image
                    } else if ("Ash".equals(weatherMain)) {
                        linearLayout.setBackgroundResource(R.drawable.ash); // Path to ash image
                    } else if ("Squall".equals(weatherMain)) {
                        linearLayout.setBackgroundResource(R.drawable.squall); // Path to squall image
                    } else if ("Tornado".equals(weatherMain)) {
                        linearLayout.setBackgroundResource(R.drawable.tornado); // Path to tornado image
                    } else {
                        linearLayout.setBackgroundColor(255); // Default image for unknown weather
                    }

                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(@NonNull Call<Root> call, @NonNull Throwable t) {
            }
        });
}
}















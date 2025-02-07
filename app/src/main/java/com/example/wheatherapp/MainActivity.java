package com.example.wheatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
SearchView searchView;
TextView locations, temps,feels, high_temp, low_temp,sunrises,sunsets,user_name,location_cord,pressureText, humidityText, visibilityText, windText, cloudCoverText;
Api_call apiCall;
SharedPreferences sharedPreferences;
FirebaseAuth firebaseAuth;
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
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        sharedPreferences = getSharedPreferences("WeatherAppPrefs", Context.MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
       user_name = findViewById(R.id.app_title);
       temps = findViewById(R.id.temps);
 linearLayout = findViewById(R.id.main);
 searchView = findViewById(R.id.search);
 feels =findViewById(R.id.feeling);
 low_temp =findViewById(R.id.low_temp);
 high_temp = findViewById(R.id.high_temp);
 sunrises = findViewById(R.id.sunrise);
 sunsets = findViewById(R.id.sunset);
 locations =findViewById(R.id.location);
location_cord =findViewById(R.id.location_coordinate);
        pressureText = findViewById(R.id.Pressure);
        humidityText = findViewById(R.id.Humidity);
        visibilityText = findViewById(R.id.Visibility);
        windText = findViewById(R.id.Wind);
        cloudCoverText = findViewById(R.id.CloudCover);
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                String userName = userEmail.replace("@gmail.com", ""); // Remove "@gmail.com"
                user_name.setText(userName);
            }
        } else {
            user_name.setText("No user is signed in.");
        }

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
                return false;
            }
        });
    }
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
                    String country = weatherData.sys.country;
                    String citys = weatherData.name;
                    locations.setText(country+","+citys);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("location", city);
                    editor.apply();
                    long sunriseTime = weatherData.sys.sunrise * 1000L;  // Convert to milliseconds
                    long sunsetTime = weatherData.sys.sunset * 1000L;
                    double Latitude = weatherData.coord.lat;
                    double  Longitude = weatherData.coord.lon;
                    String pressure = weatherData.main.pressure + " hPa";
                    String humidity = weatherData.main.humidity + "%";
                    String visibility = (weatherData.visibility / 1000.0) + " km";
                    String wind = weatherData.wind.speed + " m/s from " + weatherData.wind.deg + "°";
                    String cloudCover = weatherData.clouds.all + "%";
                    pressureText.setText("Pressure: " + pressure);
                    humidityText.setText("Humidity: " + humidity);
                    visibilityText.setText("Visibility: " + visibility);
                    windText.setText("Wind: " + wind);
                    cloudCoverText.setText("Cloud Cover: " + cloudCover);
                    location_cord.setText("Latitude:"+Latitude+"\n"+"Longitude:"+Longitude);
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    Date sunriseDate = new Date(sunriseTime);
                    Date sunsetDate = new Date(sunsetTime);
                    sunrises.setText("Sunrise: " + sdf.format(sunriseDate));
                    sunsets.setText("Sunset: " + sdf.format(sunsetDate));

                    // Convert to milliseconds
                    String weatherMain = weatherData.weather.get(0).main;
                    setWeatherBackground(weatherMain);

                    if (response.code() == 404) {
                        Toast.makeText(MainActivity.this, "City not found. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(@NonNull Call<Root> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch weather data. Please check your connection.", Toast.LENGTH_SHORT).show();

            }
        });

}
    private void setWeatherBackground(String weatherMain) {
        // Define a HashMap to map weather conditions to background resources
        Map<String, Integer> weatherBackgroundMap = new HashMap<>();
        weatherBackgroundMap.put("Clear", R.drawable.clear_sky);
        weatherBackgroundMap.put("Clouds", R.drawable.colud_background);
        weatherBackgroundMap.put("Rain", R.drawable.rain_background);
        weatherBackgroundMap.put("Drizzle", R.drawable.drizzle);
        weatherBackgroundMap.put("Thunderstorm", R.drawable.thunderstorm);
        weatherBackgroundMap.put("Snow", R.drawable.snow_background);
        weatherBackgroundMap.put("Mist", R.drawable.mist);
        weatherBackgroundMap.put("Haze", R.drawable.haze);
        weatherBackgroundMap.put("Fog", R.drawable.fog);
        weatherBackgroundMap.put("Smoke", R.drawable.smoke);
        weatherBackgroundMap.put("Dust", R.drawable.dust);
        weatherBackgroundMap.put("Sand", R.drawable.sand);
        weatherBackgroundMap.put("Ash", R.drawable.ash);
        weatherBackgroundMap.put("Squall", R.drawable.squall);
        weatherBackgroundMap.put("Tornado", R.drawable.tornado);

        // Get the background resource from the map, or use a default color if not found
        Integer backgroundResource = weatherBackgroundMap.getOrDefault(weatherMain, null);
        if (backgroundResource != null) {
            linearLayout.setBackgroundResource(backgroundResource);
        } else {
            linearLayout.setBackgroundColor(255); // Default color for unknown weather
        }
    }
}
//https://api.openweathermap.org/data/2.5/weather?q=thane&appid=a4988bdf68ba20c14a4dd36f38dd498d














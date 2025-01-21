

# Weather App

A simple and efficient Android weather application that provides real-time weather data using the OpenWeather API. The app allows users to check the weather by entering their city name, and it provides details such as temperature, humidity, and wind speed.

## Features

- **Login and Signup**: Firebase Authentication for user login and signup.
- **Weather Information**: Displays weather details such as temperature, humidity, wind speed, and weather conditions.
- **API Integration**: Fetches weather data from OpenWeather API using Retrofit.
- **Firebase Integration**: Allows saving user data in Firebase Authentication and Firebase Realtime Database.
- **Dark Mode Support**: Automatically switches between dark and light mode based on system preferences.
- **UI**: Modern, user-friendly interface with Material Design components.
  
## Screenshots

> Include any relevant screenshots of the app here.

---

## Setup Instructions

### Prerequisites

Before you begin, ensure you have the following installed:

- **Android Studio**: IDE for Android development.
- **Firebase Project**: Set up Firebase for authentication and real-time database.
- **OpenWeather API Key**: You can obtain a free API key from [OpenWeather](https://openweathermap.org/api).

### Steps to Set Up

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/shreyas6578/weather-app.git
   cd weather-app
   ```

2. **Import the Project in Android Studio**:
   Open Android Studio, go to `File > Open`, and select the folder where you cloned the repository.

3. **Add Firebase Configuration**:
   - Go to the [Firebase Console](https://console.firebase.google.com/).
   - Create a new project and follow the instructions to add Firebase Authentication and Firebase Database.
   - Download the `google-services.json` file and place it in the `app/` directory of your project.

4. **Set Up OpenWeather API**:
   - Sign up for an account on [OpenWeather](https://openweathermap.org/).
   - Get your API key.
   - Add your API key to the Retrofit `Api_call` interface.

5. **Build and Run the Project**:
   - Build the project and run it on an emulator or physical device.
   - You should be able to sign up, log in, and view weather data by entering a city name.

---

## Dependencies

Here are the key dependencies used in the project:

- **Firebase**:
  - Firebase Authentication
  - Firebase Realtime Database
  - Firebase Storage
  - Firebase Analytics
  
- **Google Play Services**:
  - Google Maps API
  - Google Sign-In

- **Networking**:
  - Retrofit: A type-safe HTTP client for Android.
  - Gson Converter: For JSON serialization/deserialization.

- **UI Components**:
  - Lottie for animations
  - Material Design components
  - RecyclerView
  - ConstraintLayout

- **Testing**:
  - JUnit for unit testing
  - Espresso for UI testing

You can find the dependencies in the `build.gradle` files of the project.

## Code Structure

- **MainActivity.java**: The main screen of the app, where users can input their city name and see weather data.
- **Login_page.java**: Firebase Authentication login page.
- **Sign_page.java**: Firebase Authentication sign-up page.
- **Splash_Screen.java**: Displays the splash screen before navigating to the login page.
- **Api_call.java**: Retrofit interface for making API calls to fetch weather data.
- **Root.java**: Data model for parsing the weather API response.


## License

This project is licensed under the MIT License.

---

This should give a comprehensive view of your Weather App project, from setup to dependencies. Would you like to add or modify any details in the `README.md`?

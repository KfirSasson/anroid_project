package com.example.kotlinfinal

import WeatherApiService
import WeatherResponse
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class WeatherFragment : Fragment() {

    private lateinit var textView: TextView
    private lateinit var cityInput: EditText
    private lateinit var getWeatherButton: Button
    private val apiKey = "e7f76a8da655b67c9e930dbe27973886"  // Replace with your OpenWeatherMap API key
    private val baseUrl = "https://api.openweathermap.org/data/2.5/"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        // Initialize UI elements
        textView = view.findViewById(R.id.textView)
        cityInput = view.findViewById(R.id.cityInput)
        getWeatherButton = view.findViewById(R.id.getWeatherButton)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherService = retrofit.create(WeatherApiService::class.java)

        // Set button click listener
        getWeatherButton.setOnClickListener {
            val city = cityInput.text.toString()

            // Check if the city input is not empty
            if (city.isNotEmpty()) {
                // Make the API call
                weatherService.getWeather(city, apiKey).enqueue(object : Callback<WeatherResponse> {
                    override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                        if (response.isSuccessful) {
                            val weather = response.body()
                            weather?.let {
                                // Update UI with weather info
                                textView.text = "City: ${it.name}\nTemp: ${it.main.temp}°C\nDescription: ${it.weather[0].description}"
                            }
                        } else {
                            Toast.makeText(requireContext(), "Failed to get weather", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // Show a message if the input is empty
                Toast.makeText(requireContext(), "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
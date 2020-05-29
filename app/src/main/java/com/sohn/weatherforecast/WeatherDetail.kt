package com.sohn.weatherforecast

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject



class WeatherDetail : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_detail)
        val intent = intent
        var weatherlist = mutableListOf<String>()

        val data = intent.getStringExtra("data")
        val position = intent.getIntExtra("position",0)

        val json = JSONObject(data)
        val dailydata = json.getJSONArray("daily")


        val current_data = dailydata.getJSONObject(position)

        val weatherArray = current_data.getJSONArray("weather")
        val weatherObject = weatherArray.getJSONObject(0)
        //main
        val temp = (current_data["temp"] as JSONObject)

        //weather
        val weathermain = weatherObject.getString("main")//Cloud
        var tmp = "Weather : $weathermain"
        weatherlist.add(tmp)

        //mintemp
        val mintemp = (temp.getDouble("min")-273.15).toInt().toString()
        tmp = "Minimum temperature : $mintemp"
        weatherlist.add(tmp)

        //maxtemp
        val maxtemp = (temp.getDouble("max")-273.15).toInt().toString()
        tmp = "Maximum temperature : $maxtemp"
        weatherlist.add(tmp)

        //humidity
        val humidity = current_data.getString("humidity")
        tmp = "Humidity : $humidity%"
        weatherlist.add(tmp)

        val sdf = SimpleDateFormat("HH:mm")
        //sunrise
        val srobject = current_data.getInt("sunrise")+32400
        val srtime = sdf.format(srobject*1000L)
        tmp = "sunrise : $srtime"
        weatherlist.add(tmp)
        //sunset
        val ssobject = current_data.getInt("sunset")+32400
        val sstime = sdf.format(ssobject*1000L)
        tmp = "sunset : $sstime"
        weatherlist.add(tmp)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, weatherlist)
        val listview = findViewById<ListView>(R.id.weather_detail)
        listview.adapter = adapter

        }


}

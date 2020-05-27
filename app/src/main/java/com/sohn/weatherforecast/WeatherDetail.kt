package com.sohn.weatherforecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject


class WeatherDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_detail)
        val intent = intent
        var weatherlist = mutableListOf<String>()
        //val view = inflater.inflate(R.layout.activity_weather_detail,null)
        val data = intent.getStringExtra("data")
        val position = intent.getIntExtra("position",0)

        val json = JSONObject(data)
        val dailydata = json.getJSONArray("daily")


        val current_data = dailydata.getJSONObject(position)

        val weatherArray = current_data.getJSONArray("weather")
        val weatherObject = weatherArray.getJSONObject(0)
        //main
        val temp = (current_data["temp"] as JSONObject)

        val weathermain = weatherObject.getString("main")//Cloud
        val mintemp = temp.getString("min")
        println(mintemp)
        val maxtemp = temp.getString("max")
        val listtitle = "$weathermain - $mintemp / $maxtemp"
        weatherlist.add(listtitle)


//        weatherlist.add("aaa")
//        weatherlist.add(data)
//        weatherlist.add("bbb\nsss\nsss")
//        println(weatherlist)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, weatherlist)
        val listview = findViewById<ListView>(R.id.weather_detail)
        listview.adapter = adapter

        }


}

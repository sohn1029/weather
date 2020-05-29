package com.sohn.weatherforecast

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result;
import org.json.JSONObject
import kotlinx.coroutines.*
import org.json.JSONArray

const val API_KEY : String = BuildConfig.ApiKey
const val BASE_URL = "http://api.openweathermap.org/data/2.5/onecall"
var jsondata : String? = ""

val city = arrayOf(
    arrayOf("37.57","126.98"),
    arrayOf("36.33","127.42"),
    arrayOf("35.87","128.59"),
    arrayOf("35.10","129.04")
)

var currentlat = "37.57"
var currentlon = "126.98"

class ForecastFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.forecast_listview,null)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = "$BASE_URL?lat=$currentlat&lon=$currentlon&exclude=current,minuetly,hourly&appid=$API_KEY"
        var weatherlist = mutableListOf<String>()

        val adapter = ArrayAdapter(this.context!!,android.R.layout.simple_list_item_1,weatherlist)
        val listview = view.findViewById<ListView>(R.id.forecast_list)
        listview.adapter = adapter
        println(currentlat)
        println(currentlon)
        GlobalScope.launch {

            var job = async {
                val (request, response, result) = url
                    .httpGet()
                    .responseString()
                result
            }

            var result = job.await()

            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    println(ex)
                }
                is Result.Success -> {
                    val data = result.get()
                    jsondata = data
                    val json = JSONObject(data)
                    val dailydata = json.getJSONArray("daily")
                    //weather array
                    for(day in 0..7){

                        val current_data = dailydata.getJSONObject(day)

                        val weatherArray = current_data.getJSONArray("weather")
                        val weatherObject = weatherArray.getJSONObject(0)
                        //main
                        val temp = (current_data["temp"] as JSONObject)

                        val sdf = SimpleDateFormat("YYYY/MM/dd")
                        val dateobject = current_data.getInt("dt")
                        val date = sdf.format(dateobject*1000L)

                        val weathermain = weatherObject.getString("main")//Cloud
                        val mintemp = (temp.getDouble("min")-273.15).toInt().toString()
                        val maxtemp = (temp.getDouble("max")-273.15).toInt().toString()
                        val listtitle = "$date  $weathermain - $mintemp / $maxtemp"
                        weatherlist.add(listtitle)

                    }

                }

            }
            activity?.runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }

        listview.setOnItemClickListener { parent, view, position, id ->
            val element = adapter.getItem(position) // The item that was clicked
            val intent = Intent(this.context, WeatherDetail::class.java)
            intent.putExtra("position",position)
            intent.putExtra("data",jsondata)
            startActivity(intent)

        }

    }


}




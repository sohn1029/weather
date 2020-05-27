package com.sohn.weatherforecast

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result;
import org.json.JSONObject
import kotlinx.coroutines.*
import org.json.JSONArray

const val API_KEY : String = BuildConfig.ApiKey
const val BASE_URL = "http://api.openweathermap.org/data/2.5/weather"
val city = listOf(
    "Seoul",
    "Daejeon",
    "Taegu",
    "Busan"
)

class ForecastFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.forecast_listview,null)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = "$BASE_URL?q=Seoul,kr&APPID=$API_KEY"
        var weatherlist = mutableListOf<String>()

        val adapter = ArrayAdapter(this.context!!,android.R.layout.simple_list_item_1,weatherlist)
        val listview = view.findViewById<ListView>(R.id.forecast_list)
        listview.adapter = adapter

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
                    val json = JSONObject(data)
                    //weather array
                    val weatherArray = json.getJSONArray("weather")
                    val weatherObject = weatherArray.getJSONObject(0)
                    //main
                    val main = (json["main"] as JSONObject)

                    val weathermain = weatherObject.getString("main")
                    val mainmintemp = main.getString("temp_min")
                    val mainmaxtemp = main.getString("temp_max")
                    val listtitle = "$weathermain-$mainmintemp/$mainmaxtemp"
                    weatherlist.add(listtitle)
                    println(weatherlist)
                }
            }
            activity?.runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }

        listview.setOnItemClickListener { parent, view, position, id ->
            val element = adapter.getItem(position) // The item that was clicked
            val intent = Intent(this.context, WeatherDetail::class.java)
            startActivity(intent)

        }
    }


}




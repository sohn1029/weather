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
const val BASE_URL = "http://api.openweathermap.org/data/2.5/onecall"

val city = arrayOf(
    arrayOf("37.57","126.98"),
    arrayOf("36.33","127.42"),
    arrayOf("35.87","128.59"),
    arrayOf("35.10","129.04")
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
        val url = "$BASE_URL?lat=37.57&lon=126.98&exclude=current,minuetly,hourly&appid=$API_KEY"
        var weatherlist = mutableListOf<String>()

        val adapter = ArrayAdapter(this.context!!,android.R.layout.simple_list_item_1,weatherlist)
        val listview = view.findViewById<ListView>(R.id.forecast_list)
        listview.adapter = adapter
        var jsondata : String? = ""

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

                        val weathermain = weatherObject.getString("main")//Cloud
                        val mintemp = temp.getString("min")
                        val maxtemp = temp.getString("max")
                        val listtitle = "$weathermain - $mintemp / $maxtemp"
                        weatherlist.add(listtitle)
                        println(weatherlist)

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




package com.sohn.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class activity_settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sps = PreferenceManager.getDefaultSharedPreferences(this)
        val editName = sps.getString("searching_city_setting", "")
        println(editName)
    }

}

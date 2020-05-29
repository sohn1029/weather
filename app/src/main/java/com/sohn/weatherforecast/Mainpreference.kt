package com.sohn.weatherforecast

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class Mainpreference : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_city)

        val cityPref = preferenceManager.findPreference<Preference>("searching_city_setting") as ListPreference

        cityPref.setOnPreferenceChangeListener { preference, newValue ->
            val index = cityPref.findIndexOfValue(newValue.toString())
            cityPref.summary = cityPref.entries[index]
            if(cityPref.summary == "Seoul"){
                currentlat = "37.57"
                currentlon = "126.98"
            }
            if(cityPref.summary == "Daejeon"){
                currentlat = "36.33"
                currentlon = "127.42"
            }
            if(cityPref.summary == "Taegu"){
                currentlat = "35.87"
                currentlon = "128.59"
            }
            if(cityPref.summary == "Busan"){
                currentlat = "35.10"
                currentlon = "129.04"
            }
            println(cityPref.summary)
            true
        }
    }

}
package com.sohn.weatherforecast

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
                .replace(R.id.forecast_fragment, ForecastFragment())
                .commit()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_items, menu)
        return true
    }

    private fun reLoadFragment() {
        // Reload current fragment
        var frg: Fragment? = null
        frg = supportFragmentManager.findFragmentById(R.id.forecast_fragment)
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.detach(frg!!)
        ft.attach(frg)
        ft.commit()


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

        if (id == R.id.refresh) {
            Toast.makeText(this, "Refresh", Toast.LENGTH_LONG).show()
            reLoadFragment()
            return true
        }
        if (id == R.id.city) {
            Toast.makeText(this, "Setting", Toast.LENGTH_LONG).show()
            val intent = Intent(this, activity_settings::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)

    }



}

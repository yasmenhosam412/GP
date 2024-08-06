package com.example.gp

import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gp.Classes.notiChatAdapter
import com.example.gp.Classes.notiItems
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class activity_notifications : AppCompatActivity() {

    private lateinit var recycle: RecyclerView
    private lateinit var dataArray: ArrayList<notiItems>
    private lateinit var adapterrrr: notiChatAdapter
    private lateinit var requestQueue: RequestQueue

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        recycle = findViewById(R.id.recycler)
        recycle.layoutManager = LinearLayoutManager(this)

        dataArray = ArrayList()
        adapterrrr = notiChatAdapter(dataArray, this)
        recycle.adapter = adapterrrr

        val sdf = SimpleDateFormat("yy/MM/dd hh:mm a", Locale("ar"))


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }


        requestQueue = Volley.newRequestQueue(this)
        val url =
            "https://api.tomorrow.io/v4/weather/forecast?location=$latitude,$longitude&apikey=QuPpq1e6gcOh3uqBstfs7U1seNyrujBp"
        val jsonObject =
            JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, { response ->
                try {
                    val timelines = response.getJSONObject("timelines")
                    val minutely = timelines.getJSONArray("minutely")


                    for (i in 0 until minutely.length()) {

                        val interval = minutely.getJSONObject(i)
                        val time = interval.getString("time")
                        val values = interval.getJSONObject("values")
                        val millis = parseISOStringToMillis(time)
                        val adjustedTime = millis + 3600000
                        val adjustedDate = Date(adjustedTime)
                        val startTime = sdf.format(adjustedDate)
                        val humidity = values.optDouble("humidity")
                        val windSpeed = values.optDouble("windSpeed")
                        val windDirection = values.optInt("windDirection")
                        val temperature = values.optDouble("temperature")
                        val dewPoint = values.optDouble("dewPoint")
                        val snowIntensity = values.optDouble("snowIntensity")
                        dataArray.add(notiItems(startTime, snowIntensity,dewPoint,temperature,windDirection,windSpeed,humidity))
                        adapterrrr.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        applicationContext,
                        "its ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()

                }


            }, {
                Toast.makeText(
                    applicationContext,
                    "not done  + ${it.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()


            })


        requestQueue.add(jsonObject)


    }


    private fun parseISOStringToMillis(isoString: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale("ar"))
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return try {
            val date = sdf.parse(isoString)
            date?.time ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    latitude = it.latitude
                    longitude = it.longitude
                    // Use latitude and longitude
                    Toast.makeText(
                        this,
                        "Latitude: $latitude, Longitude: $longitude",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to get location: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}
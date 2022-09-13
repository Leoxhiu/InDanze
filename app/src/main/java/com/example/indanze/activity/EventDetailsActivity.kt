package com.example.indanze.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.indanze.R
import com.example.indanze.databinding.ActivityEventDetailsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class EventDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityEventDetailsBinding
    private lateinit var map : GoogleMap
    lateinit var eventLatitude : String
    lateinit var eventLongitude : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Event Details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle:Bundle?=intent.extras
        val eventUserID = bundle!!.getString("firebase_eventUserID")
        val eventImage = bundle!!.getString("firebase_eventImage")
        val eventName = bundle!!.getString("firebase_eventName")
        val eventDate = bundle!!.getString("firebase_eventDate")
        val eventTime = bundle!!.getString("firebase_eventTime")
        val eventDescription = bundle!!.getString("firebase_eventDescription")
        val eventLocation = bundle!!.getString("firebase_eventLocation")
        eventLatitude = bundle!!.getString("firebase_eventLatitude").toString()
        eventLongitude = bundle!!.getString("firebase_eventLongitude").toString()

        val eventDetailImage = binding.eventDetailImage
        val eventDetailName = binding.txtEventDetailName
        val eventDetailDate = binding.txtEventDetailDate
        val eventDetailTime = binding.txtEventDetailTime
        val eventDetailDescription = binding.txtEventDetailDescription
        val eventDetailLocation = binding.txtEventDetailLocation

        Glide.with(this).load(eventImage).into(eventDetailImage)
        eventDetailName.setText(eventName)
        eventDetailDate.setText(eventDate)
        eventDetailTime.setText(eventTime)
        eventDetailDescription.setText(eventDescription)
        eventDetailLocation.setText(eventLocation)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.event_map_container) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        onBackPressed()

        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val latitude = eventLatitude.fullTrim().toDouble()
        val longitude = eventLongitude.fullTrim().toDouble()

        val eventMarker = LatLng(latitude, longitude)
        map.addMarker(MarkerOptions().position(eventMarker).title("Event Location"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(eventMarker, 15f))
    }

    private fun String.fullTrim() = trim().replace("\uFEFF", "")
}
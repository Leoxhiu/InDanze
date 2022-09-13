package com.example.indanze.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.location.Geocoder
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.indanze.R
import com.example.indanze.databinding.ActivityUpdateEventBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class UpdateEventActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
    OnMapReadyCallback {

    private lateinit var binding: ActivityUpdateEventBinding
    private lateinit var map : GoogleMap
    private var currentMarker : Marker? = null
    private lateinit var databaseReference: DatabaseReference

    lateinit var eventUserID : String
    lateinit var eventLatitude : String
    lateinit var eventLongitude : String
    lateinit var eventPosition : String

    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("d MMMM yyyy", Locale.US)
    private val timeFormatter = SimpleDateFormat("hh:mm a", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Update Event"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        databaseReference = FirebaseDatabase
            .getInstance("https://indanze-e3a7d-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("Event")

        val bundle:Bundle?=intent.extras
        eventUserID = bundle!!.getString("firebase_eventUserID").toString()
        eventPosition = bundle!!.getString("firebase_eventPosition").toString()
        val eventImage = bundle!!.getString("firebase_eventImage")
        val eventName = bundle!!.getString("firebase_eventName")
        val eventDate = bundle!!.getString("firebase_eventDate")
        val eventTime = bundle!!.getString("firebase_eventTime")
        val eventDescription = bundle!!.getString("firebase_eventDescription")
        val eventLocation = bundle!!.getString("firebase_eventLocation")
        eventLatitude = bundle!!.getString("firebase_eventLatitude").toString()
        eventLongitude = bundle!!.getString("firebase_eventLongitude").toString()

        val updateEventImage = binding.txtUpdateEventImage
        val updateEventName = binding.txtUpdateEventName
        val updateEventDate = binding.txtUpdateEventDate
        val updateEventTime = binding.txtUpdateEventTime
        val updateEventDescription = binding.txtUpdateEventDescription
        val updateEventLocation = binding.txtUpdateEventLocation

        updateEventImage.setText(eventImage)
        updateEventName.setText(eventName)
        updateEventDate.text = eventDate
        updateEventTime.text = eventTime
        updateEventDescription.setText(eventDescription)
        updateEventLocation.text = eventLocation

        pickDate()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.update_event_map_container) as SupportMapFragment
        mapFragment.getMapAsync(this)

        updateEvent()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        onBackPressed()

        return true
    }

    private fun pickDate(){

        binding.btnDateTimePicker.setOnClickListener{

            DatePickerDialog(
                this, this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year,month, dayOfMonth)
        displayFormattedDate(calendar.timeInMillis)

        TimePickerDialog(
    this, this,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

       calendar.apply {
           set(Calendar.HOUR_OF_DAY, hourOfDay)
           set(Calendar.MINUTE, minute)
       }

        displayFormattedTime(calendar.timeInMillis)
    }

    private fun displayFormattedDate(timestamp: Long){
        binding.txtUpdateEventDate.text = dateFormatter.format(timestamp)
    }

    private fun displayFormattedTime(timestamp: Long){
        binding.txtUpdateEventTime.text = timeFormatter.format(timestamp)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val latlong = LatLng(eventLatitude.fullTrim().toDouble(),eventLongitude.fullTrim().toDouble())
        drawMarker(latlong)

        map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
            override fun onMarkerDrag(p0: Marker) {

            }

            override fun onMarkerDragEnd(p0: Marker) {
                if(currentMarker != null)
                    currentMarker?.remove()

                val newLatLng = LatLng(p0.position.latitude, p0.position.longitude)
                drawMarker((newLatLng))

                eventLatitude = p0.position.latitude.toString()
                eventLongitude = p0.position.longitude.toString()
                binding.txtUpdateEventLocation.text = getAddress(p0.position.latitude,p0.position.longitude)
            }

            override fun onMarkerDragStart(p0: Marker) {

            }


        })
    }

    private fun drawMarker(latlong : LatLng){

        val markerOption = MarkerOptions().position(latlong).title("Event Location")
            .snippet(getAddress(latlong.latitude, latlong.longitude)).draggable(true)

        map.animateCamera(CameraUpdateFactory.newLatLng(latlong))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, 15f))
        currentMarker = map.addMarker(markerOption)
        currentMarker?.showInfoWindow()
    }

    private fun getAddress(latitude : Double, longitude : Double): String?{
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(latitude, longitude, 1)
        return address[0].getAddressLine(0).toString()
    }

    private fun String.fullTrim() = trim().replace("\uFEFF", "")

    private fun updateEvent(){

        binding.btnUpdate.setOnClickListener{

            val image : String = binding.txtUpdateEventImage.text.toString()
            val name : String = binding.txtUpdateEventName.text.toString()
            val date : String = binding.txtUpdateEventDate.text.toString()
            val time : String = binding.txtUpdateEventTime.text.toString()
            val description : String = binding.txtUpdateEventDescription.text.toString()
            val location : String = binding.txtUpdateEventLocation.text.toString()

            if(TextUtils.isEmpty(image)){

                Toast.makeText(this, "Image url is required", Toast.LENGTH_SHORT).show()

            }
            else if(TextUtils.isEmpty(name)){

                Toast.makeText(this, "Event name is required", Toast.LENGTH_SHORT).show()

            }
            else if(TextUtils.isEmpty(description)){

                Toast.makeText(this, "Event description is required", Toast.LENGTH_SHORT).show()

            }
            else {

                val event = mapOf(
                    "userID" to eventUserID,
                    "image" to image,
                    "name" to name,
                    "date" to date,
                    "time" to time,
                    "description" to description,
                    "location" to location,
                    "latitude" to eventLatitude,
                    "longitude" to eventLongitude
                )

                databaseReference.child(eventPosition).updateChildren(event)
                    .addOnSuccessListener {
                        onBackPressed()
                        Toast.makeText(this,
                            "Event Updated", Toast.LENGTH_SHORT).show()
                    }

            }

        }

    }

}


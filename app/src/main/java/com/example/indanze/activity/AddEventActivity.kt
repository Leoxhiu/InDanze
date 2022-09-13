package com.example.indanze.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.example.indanze.R
import com.example.indanze.databinding.ActivityAddEventBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AddEventActivity : AppCompatActivity() , DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
    OnMapReadyCallback {

    private lateinit var binding: ActivityAddEventBinding
    private lateinit var map : GoogleMap
    private var currentMarker : Marker? = null

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    lateinit var eventUserID : String
    lateinit var eventLatitude : String
    lateinit var eventLongitude : String

    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("d MMMM yyyy", Locale.US)
    private val timeFormatter = SimpleDateFormat("hh:mm a", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.create_event)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase
            .getInstance("https://indanze-e3a7d-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("Event")

        binding.txtAddEventDate.text = ""
        binding.txtAddEventTime.text = ""
        binding.txtAddEventLocation.text = ""

        pickDate()

        eventLatitude = "3.1569"
        eventLongitude = "101.7123"
        val mapFragment = supportFragmentManager.findFragmentById(R.id.add_event_map_container) as SupportMapFragment
        mapFragment.getMapAsync(this)

        addEvent()
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
        binding.txtAddEventDate.text = dateFormatter.format(timestamp)
    }

    private fun displayFormattedTime(timestamp: Long){
        binding.txtAddEventTime.text = timeFormatter.format(timestamp)
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
                binding.txtAddEventLocation.text = getAddress(p0.position.latitude,p0.position.longitude)
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

    private fun addEvent(){

        binding.btnAddEvent.setOnClickListener {

            val image : String = binding.txtAddEventImage.text.toString()
            val name : String = binding.txtAddEventName.text.toString()
            val date : String = binding.txtAddEventDate.text.toString()
            val time : String = binding.txtAddEventTime.text.toString()
            val description : String = binding.txtAddEventDescription.text.toString()
            val location : String = binding.txtAddEventLocation.text.toString()

            if(TextUtils.isEmpty(image)){

                Toast.makeText(this, "Image url is required", Toast.LENGTH_SHORT).show()

            }
            else if(TextUtils.isEmpty(name)){

                Toast.makeText(this, "Event name is required", Toast.LENGTH_SHORT).show()

            }
            else if(TextUtils.isEmpty(date)){

                Toast.makeText(this, "Event date is required", Toast.LENGTH_SHORT).show()

            }
            else if(TextUtils.isEmpty(time)){

                Toast.makeText(this, "Event time is required", Toast.LENGTH_SHORT).show()

            }
            else if(TextUtils.isEmpty(description)){

                Toast.makeText(this, "Event description is required", Toast.LENGTH_SHORT).show()

            }
            else if(TextUtils.isEmpty(location)){

                Toast.makeText(this, "Event location is required", Toast.LENGTH_SHORT).show()

            }
            else {

                val currentUser = firebaseAuth.currentUser

                if (currentUser != null) {

                    eventUserID = currentUser.uid
                }

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

                databaseReference.push().setValue(event).addOnSuccessListener {

                    Toast.makeText(this, "Event created", Toast.LENGTH_SHORT).show()

                    onBackPressed()

                }
                    .addOnFailureListener{
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }

            }
        }
    }

    private fun String.fullTrim() = trim().replace("\uFEFF", "")

}
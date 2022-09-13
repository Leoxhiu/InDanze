package com.example.indanze.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.indanze.R
import com.example.indanze.adapter.UserEventAdapter
import com.example.indanze.data.Event
import com.example.indanze.databinding.ActivityUserEventBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserEventActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserEventBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var adapter: UserEventAdapter
    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        binding = ActivityUserEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.my_event_userEvent)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        databaseReference = FirebaseDatabase
            .getInstance("https://indanze-e3a7d-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("Event")

        eventRecyclerView = binding.userEventList
        loadUserEvent()

        floatingActionButton = binding.floatingbtnAddEvent
        addEvent()
    }

    private fun loadUserEvent(){

        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {

            val userID : String = currentUser.uid

            Log.d("TAG", userID)
            var query= databaseReference.orderByChild("userID").startAt(userID).endAt(userID+"~")

            val options = FirebaseRecyclerOptions.Builder<Event>()
            options.setQuery(query ,Event::class.java)
            options.setLifecycleOwner(this)

            adapter  = UserEventAdapter(this , options.build())
            eventRecyclerView.layoutManager = LinearLayoutManager(this)
            eventRecyclerView.itemAnimator = null

            eventRecyclerView.adapter = adapter

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        onBackPressed()

        return true
    }

    private fun addEvent(){

        floatingActionButton.setOnClickListener{

            val intent = Intent(this, AddEventActivity::class.java)
            startActivity(intent)

        }

    }

}
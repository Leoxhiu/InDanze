package com.example.indanze.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.indanze.R
import com.example.indanze.adapter.UserVideoAdapter
import com.example.indanze.data.Video
import com.example.indanze.databinding.ActivityUserVideoBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserVideoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserVideoBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var videoRecyclerView: RecyclerView
    private lateinit var adapter: UserVideoAdapter
    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        binding = ActivityUserVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.my_video_userVideo)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        databaseReference = FirebaseDatabase
            .getInstance("https://indanze-e3a7d-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("Video")

        videoRecyclerView = binding.userVideoList
        loadUserVideo()

        floatingActionButton = binding.floatingbtnAddVideo
        addVideo()

    }

    private fun loadUserVideo(){

        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {

            val userID : String = currentUser.uid

            Log.d("TAG", userID)
            var query= databaseReference.orderByChild("userID").startAt(userID).endAt(userID+"~")

            val options = FirebaseRecyclerOptions.Builder<Video>()
            options.setQuery(query , Video::class.java)
            options.setLifecycleOwner(this)

            adapter  = UserVideoAdapter(this , options.build())
            videoRecyclerView.layoutManager = LinearLayoutManager(this)
            videoRecyclerView.itemAnimator = null

            videoRecyclerView.adapter = adapter

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        onBackPressed()

        return true
    }

    private fun addVideo(){

    floatingActionButton.setOnClickListener{

        val intent = Intent(this, AddVideoActivity::class.java)
        startActivity(intent)

        }

    }
}
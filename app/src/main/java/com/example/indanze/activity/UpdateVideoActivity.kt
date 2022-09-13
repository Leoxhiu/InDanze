package com.example.indanze.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import com.example.indanze.databinding.ActivityUpdateVideoBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateVideoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUpdateVideoBinding

    private lateinit var databaseReference: DatabaseReference

    lateinit var videoUserID : String
    lateinit var videoPosition : String
    lateinit var videoVideo : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Update video"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        databaseReference = FirebaseDatabase
            .getInstance("https://indanze-e3a7d-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("Video")

        val bundle:Bundle?=intent.extras
        videoUserID = bundle!!.getString("firebase_videoUserID").toString()
        videoPosition = bundle!!.getString("firebase_videoPosition").toString()
        val videoImage = bundle!!.getString("firebase_videoImage")
        val videoName = bundle!!.getString("firebase_videoName")
        val videoDescription = bundle!!.getString("firebase_videoDescription")
        videoVideo = bundle!!.getString("firebase_videoVideo").toString()

        val updateVideoImage = binding.txtUpdateVideoImage
        val updateVideoName = binding.txtUpdateVideoName
        val updateVideoDescription = binding.txtUpdateVideoDescription

        updateVideoImage.setText(videoImage)
        updateVideoName.setText(videoName)
        updateVideoDescription.setText(videoDescription)

        updateVideo()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        onBackPressed()

        return true
    }

    private fun updateVideo(){

        binding.btnUpdate.setOnClickListener{

            val image : String = binding.txtUpdateVideoImage.text.toString()
            val name : String = binding.txtUpdateVideoName.text.toString()
            val description : String = binding.txtUpdateVideoDescription.text.toString()

            if(TextUtils.isEmpty(image)){

                Toast.makeText(this, "Image url is required", Toast.LENGTH_SHORT).show()

            }
            else if(TextUtils.isEmpty(name)){

                Toast.makeText(this, "Video name is required", Toast.LENGTH_SHORT).show()

            }
            else if(TextUtils.isEmpty(description)){

                Toast.makeText(this, "Video description is required", Toast.LENGTH_SHORT).show()

            }
            else {

                val video = mapOf(
                    "userID" to videoUserID,
                    "image" to image,
                    "name" to name,
                    "description" to description,
                    "video" to videoVideo
                )

                databaseReference.child(videoPosition).updateChildren(video)
                    .addOnSuccessListener {
                        onBackPressed()
                        Toast.makeText(this,
                            "Video Updated", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
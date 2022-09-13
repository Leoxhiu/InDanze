package com.example.indanze.activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Toast
import com.example.indanze.R
import com.example.indanze.databinding.ActivityAddVideoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddVideoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddVideoBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private val videoPickGalleryCode = 100

    lateinit var videoUserID : String
    private var videoUri : Uri? = null

    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.upload_video)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase
            .getInstance("https://indanze-e3a7d-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("Video")

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading video")
        progressDialog.setCanceledOnTouchOutside(false)

        pickVideo()
        addVideo()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        onBackPressed()

        return true
    }

    private fun pickVideo(){

        binding.btnPickVideo.setOnClickListener() {
            videoPickGallery()
        }

    }

    private fun addVideo(){

        binding.btnAddVideo.setOnClickListener{

            val image : String = binding.txtAddVideoImage.text.toString()
            val name : String = binding.txtAddVideoName.text.toString()
            val description : String = binding.txtAddVideoDescription.text.toString()

            if (videoUri == null){

                Toast.makeText(this, "No video is selected", Toast.LENGTH_SHORT).show()

            }
            else if(TextUtils.isEmpty(image)){

                Toast.makeText(this, "Image url is required", Toast.LENGTH_SHORT).show()

            }
            else if(TextUtils.isEmpty(name)){

                Toast.makeText(this, "Video name is required", Toast.LENGTH_SHORT).show()

            }
            else if(TextUtils.isEmpty(description)){

                Toast.makeText(this, "Video description is required", Toast.LENGTH_SHORT).show()

            }
            else {

                progressDialog.show()

                val storageRef = FirebaseStorage.getInstance().getReference("Video/$name")
                storageRef.putFile(videoUri!!)
                    .addOnSuccessListener { taskSnapshot ->
                        val uriTask = taskSnapshot.storage.downloadUrl
                        while (!uriTask.isSuccessful);
                        val downloadUri = uriTask.result
                        if (uriTask.isSuccessful){

                            val currentUser = firebaseAuth.currentUser

                            if (currentUser != null) {

                                videoUserID = currentUser.uid
                            }

                            val video = mapOf(
                                "userID" to videoUserID,
                                "image" to image,
                                "name" to name,
                                "description" to description,
                                "video" to downloadUri.toString()
                            )

                            databaseReference.push().setValue(video).addOnSuccessListener {

                                progressDialog.dismiss()
                                Toast.makeText(this, "Video uploaded", Toast.LENGTH_SHORT).show()
                                onBackPressed()
                            }
                                .addOnFailureListener{
                                    progressDialog.dismiss()
                                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                                }


                        }
                    }
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Upload video failed", Toast.LENGTH_SHORT).show()
                    }

            }

        }
    }

    private fun videoPickGallery(){

        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(
            Intent.createChooser(intent, "Choose video"),
            videoPickGalleryCode
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == RESULT_OK){

            if (requestCode == videoPickGalleryCode){

                videoUri = data!!.data
                Toast.makeText(this, "Video selected", Toast.LENGTH_SHORT).show()

            }

        }
        else {

            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()

        }

        super.onActivityResult(requestCode, resultCode, data)
    }

}
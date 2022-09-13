package com.example.indanze.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.indanze.R
import com.example.indanze.activity.SignInActivity
import com.example.indanze.activity.UserEventActivity
import com.example.indanze.activity.UserVideoActivity
import com.example.indanze.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference : DatabaseReference
    private lateinit var nameProfile : TextView
    private lateinit var emailProfile : TextView
    private lateinit var btn_userVideo : Button
    private lateinit var btn_userEvent : Button
    private lateinit var btn_userLogout : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_fragment_profile)
        firebaseAuth = FirebaseAuth.getInstance()

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        var view =  binding.root

        nameProfile = binding.txtNameProfile
        emailProfile = binding.txtEmailProfile
        btn_userVideo = binding.btnUserVideo
        btn_userEvent = binding.btnUserEvent
        btn_userLogout = binding.btnUserLogout

        btn_userVideo.setOnClickListener{
            val intent = Intent(activity, UserVideoActivity::class.java)
            startActivity(intent)
        }

        btn_userEvent.setOnClickListener{
            val intent = Intent(activity, UserEventActivity::class.java)
            startActivity(intent)
        }

        btn_userLogout.setOnClickListener{
            firebaseAuth.signOut()
            getUser()
        }

        getUser()

        // Inflate the layout for this fragment
        return view
    }

    companion object {

        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    private fun getUser(){

        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {

            val email = currentUser.email
            emailProfile.setText(email)

            databaseReference = FirebaseDatabase
                .getInstance("https://indanze-e3a7d-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("User")

            databaseReference.child(currentUser.uid).get().addOnSuccessListener(){

                if (it.exists()){

                    val username = it.child("username").value
                    nameProfile.setText(username.toString())

                } else {

                    Toast.makeText(this@ProfileFragment.requireActivity(), "User not found",
                        Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener(){
                Toast.makeText(this@ProfileFragment.requireActivity(), "Failed, something is wrong",
                    Toast.LENGTH_SHORT).show()
            }

        } else {

            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)

        }
    }
}



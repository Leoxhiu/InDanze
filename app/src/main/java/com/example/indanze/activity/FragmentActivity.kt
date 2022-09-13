package com.example.indanze.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.indanze.R
import com.example.indanze.databinding.ActivityFragmentBinding
import com.example.indanze.fragment.EventFragment
import com.example.indanze.fragment.ProfileFragment
import com.example.indanze.fragment.VideoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class FragmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragmentBinding
    private lateinit var bottomNav : BottomNavigationView
    private val videoFragment = VideoFragment()
    private val eventFragment = EventFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(videoFragment)

        bottomNav = binding.bottomNavigation

        bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigationVideo -> replaceFragment(videoFragment)
                R.id.navigationEvent -> replaceFragment(eventFragment)
                R.id.navigationProfile -> replaceFragment(profileFragment)
            }
            true
        }
    }


    private fun replaceFragment(fragment : Fragment){

        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }

    }

}
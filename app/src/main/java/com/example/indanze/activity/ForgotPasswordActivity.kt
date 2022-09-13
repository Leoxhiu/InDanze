package com.example.indanze.activity

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.indanze.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityForgotPasswordBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide();

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Sending...")

        binding.btnBackForgotPassword.setOnClickListener { onBackPressed() }

        binding.btnForgotPassword.setOnClickListener(){

            val email = binding.txtEmailForgotPassword.text.toString().trim()

            if (email.isNotEmpty()) {

                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                    progressDialog.show()
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener{

                        if (it.isSuccessful) {
                            progressDialog.dismiss()
                            Toast.makeText(this,
                                "Reset password email is sent", Toast.LENGTH_SHORT).show()
                            onBackPressed()

                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(this,
                                "Please try again, something is wrong", Toast.LENGTH_SHORT).show()
                        }

                    }

                }
                else {

                    binding.txtEmailForgotPassword.error = "Invalid email"

                }

            } else {
                Toast.makeText(this, "Please fill in your email", Toast.LENGTH_SHORT).show()
                binding.txtEmailForgotPassword.requestFocus()
            }

        }

    }

}
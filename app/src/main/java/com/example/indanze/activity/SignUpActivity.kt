package com.example.indanze.activity

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.indanze.data.User
import com.example.indanze.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var progressDialog: ProgressDialog
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference : DatabaseReference

    private var email = ""
    private var password = ""
    private var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide();

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Creating account...")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.lblSignIn.setOnClickListener(){
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignUp.setOnClickListener{
            dataValidation()
        }

    }

    private fun dataValidation(){

        username = binding.txtUsernameSignUp.text.toString().trim()
        email = binding.txtEmailSignup.text.toString().trim()
        password = binding.txtPasswordSignup.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()){

            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                if (password.length < 6) {

                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                }
                else{
                        firebaseSignUp()
                    }
            }
            else {
                binding.txtEmailSignup.error = "Invalid email"
            }
        } else {
            Toast.makeText(this, "Please fill in the empty fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseSignUp(){
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task->
            if (task.isSuccessful) {
                progressDialog.dismiss()

                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email

                val newUser = User(username, email)
                databaseReference = FirebaseDatabase
                    .getInstance("https://indanze-e3a7d-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("User")
                databaseReference.child(firebaseUser.uid).setValue(newUser).addOnCompleteListener {

                        if (it.isSuccessful){

                            Toast.makeText(this,
                                "Sign up successfully with $email", Toast.LENGTH_SHORT).show()

                            startActivity(Intent(this, SignInActivity::class.java))
                            finish()

                        } else {
                            Toast.makeText(this,
                                "Database problem", Toast.LENGTH_SHORT).show()
                        }

                    }

            } else {
                progressDialog.dismiss()
                Toast.makeText(this,
                    "Sign up failed, please try again, due to ${task.exception}", Toast.LENGTH_LONG).show()
                    Log.i(TAG, "${task.exception}");
            }
        }
    }

}
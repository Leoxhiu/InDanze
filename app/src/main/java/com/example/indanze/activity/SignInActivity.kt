package com.example.indanze.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.indanze.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide();

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Logging in...")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.lblSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignIn.setOnClickListener {
            dataValidation()

        }

        binding.lblForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun dataValidation(){
        email = binding.txtEmail.text.toString().trim()
        password = binding.txtPassword.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty()) {

            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                firebaseLogin()

            }
            else {

                binding.txtEmail.error = "Invalid email"

            }

        } else {
            Toast.makeText(this, "Please fill in the empty fields", Toast.LENGTH_SHORT).show()
        }

    }

    private fun firebaseLogin(){
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {

            if (it.isSuccessful) {
                progressDialog.dismiss()
                Toast.makeText(this,
                    "Sign in successfully", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, FragmentActivity::class.java))
                finish()
            } else {
                progressDialog.dismiss()
                Toast.makeText(this,
                    "Account not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkUser(){
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            startActivity(Intent(this, FragmentActivity::class.java))
            finish()
        }
    }
}
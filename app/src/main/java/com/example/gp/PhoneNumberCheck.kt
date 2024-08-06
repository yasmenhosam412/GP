package com.example.gp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Process
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class PhoneNumberCheck : AppCompatActivity() {
    public lateinit var btn4: Button
    public lateinit var btn3: Button
    public lateinit var edt: EditText
    public lateinit var auth: FirebaseAuth
    public lateinit var credentiall: PhoneAuthCredential
    var storedVerificationId: String? = null
    public lateinit var pb3: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number_check)
        btn4 = findViewById(R.id.button4)
        btn3 = findViewById(R.id.button3)
        edt = findViewById(R.id.editTextText)
        auth = FirebaseAuth.getInstance()
        storedVerificationId = intent.getStringExtra("storedVerificationId")
        pb3 = findViewById(R.id.progressBar3)
        btn4.setOnClickListener {
            val intent: Intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }


        btn3.setOnClickListener {
            val otp = edt.text.trim().toString()
            if (otp.isNotEmpty()) {
                credentiall = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp
                )
                signInWithPhoneAuthCredential(credentiall)
            } else {
                Toast.makeText(this, "برجاء ادخال الكود", Toast.LENGTH_SHORT).show()
            }
        }
    }

    public fun signInWithPhoneAuthCredential(credentiall: PhoneAuthCredential) {
        pb3.visibility = View.VISIBLE
        auth.signInWithCredential(credentiall).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(applicationContext, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT)
                    .show()
                val intent: Intent = Intent(this, MainMainActivity::class.java)
                startActivity(intent)
                finish()
                pb3.visibility = View.INVISIBLE
            } else {
                if (it.exception is FirebaseAuthInvalidCredentialsException) {

                    pb3.visibility = View.INVISIBLE
                    Toast.makeText(
                        this,
                        "برجاء ادخال الكود بشكل صحيح" + it.exception,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }

    }

}



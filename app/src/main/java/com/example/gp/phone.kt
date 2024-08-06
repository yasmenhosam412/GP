package com.example.gp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class phone : AppCompatActivity() {
    public lateinit var edt: EditText
    public lateinit var btn: Button


    public lateinit var authh: FirebaseAuth
    public lateinit var db: FirebaseFirestore
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    public lateinit var number: String
    public lateinit var credential: PhoneAuthCredential
    public lateinit var pb4 : ProgressBar


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)


        db = FirebaseFirestore.getInstance()
        pb4=findViewById(R.id.progressBar4)
        btn = findViewById(R.id.button21)
        edt = findViewById(R.id.editTextPhone)
        authh = FirebaseAuth.getInstance()



        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, PhoneNumberCheck::class.java)) //check
                finish()
                Toast.makeText(
                    applicationContext,
                    "تم ارسال الرمز الي رقم الهاتف",
                    Toast.LENGTH_SHORT
                ).show()
                pb4.visibility=View.INVISIBLE
            }
            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(
                    applicationContext,
                    "فشل الارسال برجاء اعادة المحاولة في وقت لاحق",
                    Toast.LENGTH_SHORT
                ).show()
                pb4.visibility=View.INVISIBLE
            }
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                storedVerificationId = verificationId
                resendToken = token
                val intent = Intent(applicationContext, PhoneNumberCheck::class.java)
                intent.putExtra("storedVerificationId", storedVerificationId)
                startActivity(intent)
                finish()
                Toast.makeText(
                    applicationContext,
                    "تم ارسال الرمز الي رقم الهاتف",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        btn.setOnClickListener {
            if (edt.text.matches(Patterns.PHONE.toRegex())) {
                number = edt.text.trim().toString()
                number = "+2$number"
                credential = PhoneAuthProvider.getCredential(
                    edt.text.toString(), number
                )
                sendVerificationCode(number)

            } else {
                Toast.makeText(
                    applicationContext,
                    "برجاء ادخال رقم الهاتف",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun sendVerificationCode(number: String) {
        var phonee: String = edt.text.toString()
        pb4.visibility=View.VISIBLE
        if (phonee.isNullOrBlank()) {
            Toast.makeText(
                applicationContext,
                "برجاء ادخال رقم الهاتف ",
                Toast.LENGTH_SHORT
            ).show()

        } else {
            val options = PhoneAuthOptions.newBuilder(authh)
                .setPhoneNumber(number)
                .setTimeout(120L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }




    override fun onBackPressed() {
        super.onBackPressed()
        var intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
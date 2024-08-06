package com.example.gp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.system.measureTimeMillis


class HomeActivity : AppCompatActivity() {
    public lateinit var btn1: Button
    public lateinit var btn2: Button
    public lateinit var btn: Button
    public lateinit var txtview5: TextView
    public lateinit var edtemail: EditText
    public lateinit var edtpassword: EditText
    public lateinit var auth: FirebaseAuth
    public lateinit var txt7: TextView
    public lateinit var pb: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btn1 = findViewById(R.id.button)
        btn2 = findViewById(R.id.button2)
        btn = findViewById(R.id.button)
        txtview5 = findViewById(R.id.textView5)
        edtemail = findViewById(R.id.editTextTextEmailAddress)
        edtpassword = findViewById(R.id.editTextTextPassword)
        auth = FirebaseAuth.getInstance()
        txt7 = findViewById(R.id.textView7)
        pb = findViewById(R.id.progressBar)




        txt7.setOnClickListener {
            var intent = Intent(this, phone::class.java)
            startActivity(intent)
        }


        btn.setOnClickListener {
            if (edtemail.text.matches(Patterns.EMAIL_ADDRESS.toRegex())) {

                create()

            } else {
                Toast.makeText(
                    applicationContext,
                    "برجاء ادخال البيانات بشكل صحيح",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        txtview5.setOnClickListener {

            var intent: Intent = Intent(this, activity_change_password::class.java)
            startActivity(intent)
            finish()
        }

        btn2.setOnClickListener {
            var intent: Intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun create() {
        var email: String = edtemail.text.toString()
        var passsword: String = edtpassword.text.toString()
        if (email.isNullOrBlank()) {
            Toast.makeText(applicationContext, "برجاء ادخال بريد الكتروني ", Toast.LENGTH_SHORT)
                .show()
        } else if (passsword.isNullOrBlank()) {
            Toast.makeText(applicationContext, "برجاء ادخال كلمة سر ", Toast.LENGTH_SHORT)
                .show()
        } else {
            pb.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(email, passsword).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "تم تسجيل الدخول بنجاح",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    var intent = Intent(this, MainMainActivity::class.java)
                    startActivity(intent)
                    finish()
                    pb.visibility = View.INVISIBLE
                } else {
                    pb.visibility = View.INVISIBLE

                    Toast.makeText(
                        applicationContext,
                        "فشلت المحاولة , برجاء المحاولة مرة اخري",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        var intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }


}





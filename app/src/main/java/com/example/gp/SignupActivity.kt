package com.example.gp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit


class SignupActivity : AppCompatActivity() {
    public lateinit var btn5: Button
    public lateinit var edt3email: EditText
    public lateinit var edt1passwoed: EditText
    public lateinit var edt3passwordC: EditText
    public lateinit var edtname: EditText
    public lateinit var edtfamily: EditText


    public lateinit var authh: FirebaseAuth
    public lateinit var db: FirebaseFirestore

    public lateinit var pb2:ProgressBar




    @SuppressLint("SuspiciousIndentation", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        db = FirebaseFirestore.getInstance()



        pb2=findViewById(R.id.progressBar2)

        btn5 = findViewById(R.id.button5)

        edt3email = findViewById(R.id.editTextTextEmailAddress3)
        edt1passwoed = findViewById(R.id.editTextNumberPassword)
        edt3passwordC = findViewById(R.id.editTextNumberPassword3)

        edtname = findViewById(R.id.editTextTextEmailAddress4)
        edtfamily = findViewById(R.id.editTextTextEmailAddress5)

        authh = FirebaseAuth.getInstance()







        btn5.setOnClickListener {

           if (edt3email.text.matches(Patterns.EMAIL_ADDRESS.toRegex())) {

                create()

            } else {
                Toast.makeText(
                    applicationContext,
                    "برجاء ادخال البيانات بشكل صحيح",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }


    }



    private fun create() {
        var email: String = edt3email.text.toString()
        var passsword: String = edt1passwoed.text.toString()
        var name: String = edtname.text.toString()
        var family: String = edtfamily.text.toString()

        if (name.isNullOrBlank()) {
            Toast.makeText(
                applicationContext,
                "برجاء ادخال الاسم الاول ",
                Toast.LENGTH_SHORT
            ).show()
        } else if (family.isNullOrBlank()) {
            Toast.makeText(
                applicationContext,
                "برجاء ادخال اسم العائلة ",
                Toast.LENGTH_SHORT
            ).show()

        } else if (email.isNullOrBlank()) {
            Toast.makeText(
                applicationContext,
                "برجاء ادخال بريد الكتروني او رقم الهاتف ",
                Toast.LENGTH_SHORT
            ).show()
        } else if (passsword.isNullOrBlank()) {
            Toast.makeText(applicationContext, "برجاء ادخال كلمة سر ", Toast.LENGTH_SHORT).show()

        } else if (edt3passwordC.text.isNullOrBlank()) {
            Toast.makeText(applicationContext, "برجاء تاكيد كلمة السر ", Toast.LENGTH_SHORT).show()
        } else if (edt1passwoed.text.toString() != edt3passwordC.text.toString()) {

            Toast.makeText(applicationContext, "كلمة السر غير متطابقة !!", Toast.LENGTH_SHORT)
                .show()
        } else {
            pb2.visibility=View.VISIBLE
            authh.createUserWithEmailAndPassword(email, passsword).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(applicationContext, "تم انشاء الحساب بنجاح", Toast.LENGTH_SHORT)
                        .show()
                    var i = Intent(this, MainMainActivity::class.java)
                    startActivity(i)
                    finish()
                    pb2.visibility=View.INVISIBLE

                } else {
                    pb2.visibility=View.INVISIBLE
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
        super.onBackPressed()
        var intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
package com.example.gp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class activity_change_password : AppCompatActivity() {
    public lateinit var btn8: Button
    public lateinit var email : EditText
    public  var auth : FirebaseAuth? =null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        btn8=findViewById(R.id.button8)
        email=findViewById(R.id.editTextTextEmailAddress2)


        auth=FirebaseAuth.getInstance()
        btn8.setOnClickListener {


           if(email.text.isNullOrEmpty()){
               Toast.makeText(applicationContext, "برجاء ادخال البريد الالكتروني الخاص بك", Toast.LENGTH_SHORT).show()
           }else{
               auth?.sendPasswordResetEmail(email.text.toString())
                   ?.addOnCompleteListener { task ->
                       if (task.isSuccessful) {
                           Toast.makeText(applicationContext, "برجاء فحص البريد الالكتروني الخاص بك", Toast.LENGTH_SHORT).show()
                           var intent : Intent = Intent(this,activity_change_password_done::class.java)
                           startActivity(intent)
                           finish()


                       } else {
                           Toast.makeText(applicationContext, "برجاء اعادة المحاولة في وقت لاحق", Toast.LENGTH_SHORT).show()
                       }
                   }
           }
        }
    }
}




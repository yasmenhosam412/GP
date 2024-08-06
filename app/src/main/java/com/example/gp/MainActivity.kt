package com.example.gp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    public lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        auth =FirebaseAuth.getInstance()


        Handler().postDelayed({
            if(auth.currentUser==null){
                var intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)
                finish()

            }else{
                var intent2 = Intent(this,MainMainActivity::class.java)
                startActivity(intent2)
                finish()

            }
        },3000)
    }

}
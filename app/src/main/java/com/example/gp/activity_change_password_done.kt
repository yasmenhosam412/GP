package com.example.gp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class activity_change_password_done : AppCompatActivity() {
    public lateinit var btn9: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password_done)

        btn9=findViewById(R.id.button9)

        btn9.setOnClickListener {
            var intent = Intent (this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
package com.example.gp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth

class information_activity : AppCompatActivity() {
    public lateinit var img19 : ImageView
    public lateinit var img11 : ImageView
    public lateinit var btn11 : AppCompatButton
    public lateinit var btn10 : AppCompatButton

    public lateinit var txt63  :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        img19=findViewById(R.id.imageView19)
        btn11=findViewById(R.id.button11)
        btn10=findViewById(R.id.button10)

        btn10.setOnClickListener {
            var intent = Intent(this ,BalanceActivity::class.java)
            startActivity(intent)

        }

        var x  = FirebaseAuth.getInstance().currentUser?.email?.substringBefore("@")
        var y  = FirebaseAuth.getInstance().currentUser?.phoneNumber

        txt63=findViewById(R.id.textView63)

        if(x==null){

            txt63.text="$y"
        }else if(y== null){
            txt63.text="$x"
        }else{
            txt63.text="$x $y"
        }



        img11=findViewById(R.id.imageView11)

        img11.setOnClickListener {
            var intent = Intent(this ,PorfileActivity::class.java)
            startActivity(intent)
        }

        btn11.setOnClickListener {
            var intent = Intent(this ,about_program::class.java)
            startActivity(intent)
        }


        img19.setOnClickListener{
            var intent = Intent(this ,activity_settings::class.java)
            startActivity(intent)

        }

    }
}
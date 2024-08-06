package com.example.gp

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class activity_settings : AppCompatActivity() {
    public lateinit var btn12: Button
    public lateinit var btn14: Button
    public lateinit var btn7: Button

    public lateinit var txt43: TextView
    public lateinit var auth: FirebaseAuth


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        txt43 = findViewById(R.id.textView43)


        btn14 = findViewById(R.id.button155)
        btn12 = findViewById(R.id.button12)
        btn7 = findViewById(R.id.button7)
        btn7.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("حذف الحساب")
                .setMessage("هل انت متاكد انك تريد حذف الحساب ؟")

            builder.setPositiveButton("نعم") { dialogInterface: DialogInterface, i: Int ->
                val user = auth.currentUser
                val userId = user?.uid

                user?.delete()
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "تم حذف الحساب", Toast.LENGTH_SHORT).show()
                            var intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "عفوا , فشلت محاولة حذف الحساب", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            builder.setNegativeButton("الغاء") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }




        auth = FirebaseAuth.getInstance()



        btn14.setOnClickListener {


            var intent = Intent(this, PorfileActivity::class.java)
            startActivity(intent)


        }


        btn12.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("تسجيل الخروج")
                .setMessage("هل انت متاكد انك تريد تسجيل الخروج ؟")

            builder.setPositiveButton("نعم") { dialogInterface: DialogInterface, i: Int ->
                var intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                auth.signOut()
                finish()
            }
            builder.setNegativeButton("الغاء") { dialogInterface: DialogInterface, i: Int ->

                dialogInterface.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }


    }


}

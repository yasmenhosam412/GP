package com.example.gp


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.icu.lang.UCharacter.NumericType
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gp.Classes.angazatItems
import com.example.gp.Classes.engazAdapter
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


class PorfileActivity : AppCompatActivity() {
    public lateinit var btn18: Button
    public lateinit var btn6: Button
    public lateinit var txt28: TextView
    public lateinit var txt299: TextView
    public lateinit var txt74: TextView
    public lateinit var txt79: TextView
    public lateinit var txt97: TextView
    public lateinit var txt98: TextView
    public lateinit var txt99: TextView

    public lateinit var db: FirebaseFirestore
    public lateinit var current: String


    private lateinit var img: ImageView


    private lateinit var img2: CircleImageView

    public lateinit var adapter: engazAdapter


    public lateinit var dataArray: ArrayList<angazatItems>


    private val PICK_IMAGE_REQUEST = 1
    private val PICK_IMAGE_REQUESTProfile = 2
    private var selectedImageUri: Uri? = null

    private var selectedImageUriProfile: String? = null

    public lateinit var editTextInput: String

    public var otherUserEmail: String? = null
    public var otherUserPhone: String? = null

    public lateinit var ratingBar2: RatingBar

    public lateinit var img18: CircleImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_porfile)

        FirebaseApp.initializeApp(this)
        current = FirebaseAuth.getInstance().currentUser?.uid.toString()
        db = FirebaseFirestore.getInstance()
        btn18 = findViewById(R.id.button18)
        btn6 = findViewById(R.id.button6)
        var recyclerview = findViewById<RecyclerView>(R.id.recyclerEng)
        recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        dataArray = ArrayList<angazatItems>()
        adapter = engazAdapter(dataArray, this)
        img18 = findViewById(R.id.img18)
        recyclerview.adapter = adapter
        ratingBar2 = findViewById(R.id.ratingBar2)


        btn6.setOnClickListener {

            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            img = ImageView(this)
            img.setImageResource(R.drawable.profile)
            img.id = View.generateViewId()

            var desiredWidth: Int = 350
            var desiredHeight: Int = 350

            var layoutParams = ViewGroup.LayoutParams(desiredWidth, desiredHeight)
            img.setLayoutParams(layoutParams)

            layout.gravity = Gravity.CENTER
            layout.addView(img)

            img.setOnClickListener {

                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            }
            val inputEditTextField5 = EditText(this)
            inputEditTextField5.hint = "الانجاز"
            layout.addView(inputEditTextField5)

            var dialog =
                AlertDialog.Builder(this).setTitle("اضف انجاز").setMessage("تفاصيل الانجاز")
                    .setCancelable(false).setView(layout).setPositiveButton("اضافة") { _, _ ->
                        editTextInput = inputEditTextField5.text.toString()



                        if (editTextInput.isNotEmpty()) {

                            dataArray.add(angazatItems(selectedImageUri, editTextInput))


                            saveengazz()

                            adapter.notifyDataSetChanged()

                        } else {
                            Toast.makeText(
                                applicationContext,
                                "عفوا يجب اضافة جميع تفاصيل الانجاز",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.setNegativeButton("الغاء", null).create()
            val window = dialog.window
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawableResource(android.R.color.white)
            dialog.show()
        }


        txt28 = findViewById(R.id.textView28)

        txt299 = findViewById(R.id.textView299)
        txt74 = findViewById(R.id.textView74)
        txt79 = findViewById(R.id.textView79)
        txt97 = findViewById(R.id.textView97)
        txt98 = findViewById(R.id.textView98)
        txt99 = findViewById(R.id.textView299)


        val db = FirebaseFirestore.getInstance()




        otherUserEmail = intent.getStringExtra("OTHER_USER_EMAIL")
        otherUserPhone = intent.getStringExtra("OTHER_USER_PHONE")


        ratingBar2.setOnRatingBarChangeListener { _, rating, _ ->
            if (otherUserEmail != null) {
                saveProfileRating(otherUserEmail!!, rating)
            } else if (otherUserPhone != null) {
                saveProfileRating(otherUserPhone!!, rating)
            }
        }





        if (otherUserEmail != null) {
            fetchDataFromFirestoreByEmail(otherUserEmail!!)
            btn6.isEnabled = false
            btn18.isEnabled = false
            displayProfileInfo(db, otherUserEmail!!,otherUserPhone!!)
            txt28.text = otherUserEmail.toString()
        } else {
            retrieveProfileData()
            val currentUseremail = FirebaseAuth.getInstance().currentUser?.email
            var email = FirebaseAuth.getInstance().currentUser?.email
                ?: FirebaseAuth.getInstance().currentUser?.phoneNumber
            fetchProfileRating(email!!)
            fetchDataFromFirestoreByEmail(currentUseremail!!)
            ratingBar2.isEnabled = false
        }




        btn18.setOnClickListener {
            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            img2 = CircleImageView(this)
            img2.setImageResource(R.drawable.profile)
            img2.id = View.generateViewId()

            var desiredWidth: Int = 350
            var desiredHeight: Int = 350

            var layoutParams = ViewGroup.LayoutParams(desiredWidth, desiredHeight)
            img2.layoutParams = layoutParams

            layout.gravity = Gravity.CENTER
            layout.addView(img2)

            img2.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE_REQUESTProfile)
            }

            val inputEditTextField6 = EditText(this)
            inputEditTextField6.hint = "اسم المستخدم"
            layout.addView(inputEditTextField6)

            val inputEditTextField = EditText(this)
            inputEditTextField.hint = "رقم الهاتف"
            inputEditTextField.inputType = NumericType.NUMERIC
            layout.addView(inputEditTextField)

            val inputEditTextField2 = EditText(this)
            inputEditTextField2.hint = "المحاصيل"
            layout.addView(inputEditTextField2)

            val inputEditTextField3 = EditText(this)
            inputEditTextField3.hint = "طريقة البيع"
            layout.addView(inputEditTextField3)

            val inputEditTextField4 = EditText(this)
            inputEditTextField4.hint = "العنوان"
            layout.addView(inputEditTextField4)

            val inputEditTextField5 = EditText(this)
            inputEditTextField5.hint = "المنتجات المطلوبة"
            layout.addView(inputEditTextField5)


            var dialog =
                AlertDialog.Builder(this).setTitle("البيانات الشخصية").setMessage("اضف بياناتك")
                    .setCancelable(false).setView(layout).setPositiveButton("حفظ") { _, _ ->
                        val editTextInput = inputEditTextField.text.toString()
                        val editTextInput2 = inputEditTextField2.text.toString()
                        val editTextInput3 = inputEditTextField3.text.toString()
                        val editTextInput4 = inputEditTextField4.text.toString()
                        val editTextInput5 = inputEditTextField5.text.toString()
                        val editTextInput6 = inputEditTextField6.text.toString()
                        if (editTextInput.isNotEmpty() && editTextInput2.isNotEmpty() && editTextInput3.isNotEmpty()
                            && editTextInput4.isNotEmpty() && editTextInput5.isNotEmpty() && editTextInput6.isNotEmpty()
                        ) {

                            txt299.text = " رقم الهاتف : $editTextInput"
                            txt74.text = " المحاصيل : $editTextInput2"
                            txt79.text = "طريقة البيع :  $editTextInput3"
                            txt97.text = "العنوان :  $editTextInput4"
                            txt98.text = "المنتجات المطلوبة :  $editTextInput5"
                            txt28.text = "الاسم : $editTextInput6"

                            selectedImageUriProfile?.let { uriString ->
                                Glide.with(this@PorfileActivity).load(Uri.parse(uriString))
                                    .into(img18)
                            }

                            saveprofile()

                        } else {
                            Toast.makeText(
                                applicationContext,
                                "عفوا يجب اضافة جميع البيانات",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }.setNegativeButton("الغاء", null).create()
            dialog.show()


        }

    }

    private fun saveProfileRating(identifier: String, rating: Float) {
        val field = if (identifier.contains('@')) "email" else "phone"

        val ratingMap = hashMapOf(
            "rating" to rating.toDouble()
        )

        val documentId = "${field}_$identifier"

        db.collection("Ratings")
            .document(documentId)
            .set(ratingMap)
            .addOnSuccessListener {
                Toast.makeText(this, "تم حفظ التقييم بنجاح", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "فشلت محاولة حفظ التقييم",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
    private fun fetchProfileRating(identifier: String) {
        val field = if (identifier.contains('@')) "email" else "phone"

        val documentId = "${field}_$identifier"

        db.collection("Ratings")
            .document(documentId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val rating = documentSnapshot.getDouble("rating")

                    ratingBar2.rating=rating!!.toFloat()

                } else {

                    Toast.makeText(this, "لا يوجد تقييم", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->

                Toast.makeText(
                    this,
                    "فشلت محاولة استرجاع التقييم",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }


    fun fetchDataFromFirestoreByEmail(userEmail: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("engazz").whereEqualTo("email", userEmail).get()
            .addOnSuccessListener { documents ->

                for (document in documents) {
                    val image = document.getString("image")
                    val text = document.getString("engaz")
                    val imageUri: Uri? = if (!image.isNullOrEmpty()) {
                        Uri.parse(image)
                    } else {
                        null
                    }
                    dataArray.add(angazatItems(imageUri, text.toString()))
                }
                adapter.notifyDataSetChanged()
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    applicationContext, "فشلت محاولة استرجاع البيانات", Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun saveengazz() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.email

        currentUserUid?.let { email ->
            val db = FirebaseFirestore.getInstance()
            val engazzData = hashMapOf(
                "image" to selectedImageUri.toString(), "engaz" to editTextInput, "email" to email
            )

            db.collection("engazz").add(engazzData).addOnSuccessListener { documentReference ->
                Toast.makeText(
                    this, "تم حفظ البيانات بنجاح", Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener { e ->
                Toast.makeText(
                    this, "فشلت محاولة حفظ البيانات", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun displayProfileInfo(db: FirebaseFirestore, otherUserEmail: String?, otherUserPhone: String?) {
        val identifier = otherUserEmail ?: otherUserPhone
        val queryField = if (otherUserEmail != null) "email" else "phone"

        if (identifier != null) {
            fetchProfileRating(identifier)

            db.collection("profiles").whereEqualTo(queryField, identifier).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val number = document.getString("number")
                        val address = document.getString("address")
                        val mahasel = document.getString("mahasel")
                        val way = document.getString("way")
                        val required = document.getString("required")
                        val name = document.getString("Name")
                        val profileImageUrl = document.getString("profileImageUrl")

                        profileImageUrl?.let {
                            Glide.with(this@PorfileActivity).load(it).into(img18)
                        }

                        txt299.text = number
                        txt97.text = address
                        txt98.text = required
                        txt79.text = way
                        txt74.text = mahasel
                        txt28.text = name
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(this, "فشلت محاولة استرجاع البيانات", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "لم يتم توفير بريد إلكتروني أو رقم هاتف", Toast.LENGTH_SHORT).show()
        }
    }


    private fun retrieveProfileData() {

        try {
            current.let { uid ->
                db.collection("profiles").document(uid).get().addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val number = document.getString("number")
                        val address = document.getString("address")
                        val mahasel = document.getString("mahasel")
                        val way = document.getString("way")
                        val required = document.getString("required")
                        val name = document.getString("Name")
                        val profileImageUrl = document.getString("profileImageUrl")

                        txt299.text = "$number"
                        txt97.text = "$address"
                        txt98.text = "$required"
                        txt79.text = " $way"
                        txt74.text = "$mahasel"
                        txt28.text = "$name"
                        profileImageUrl?.let {
                            Glide.with(this@PorfileActivity).load(it).into(img18)
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "فشلت محاولة استرجاع البيانات", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, " فشلت المحاولة برجاء المحاولة مرة اخري", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveprofile() {
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        val currentPhone = FirebaseAuth.getInstance().currentUser?.phoneNumber
        if (currentUserEmail != null) {
            val profile = hashMapOf(
                "email" to currentUserEmail,
                "phone" to currentPhone,
                "number" to txt299.text.toString(),
                "mahasel" to txt74.text.toString(),
                "way" to txt79.text.toString(),
                "address" to txt97.text.toString(),
                "required" to txt98.text.toString(),
                "Name" to txt28.text.toString(),
                "profileImageUrl" to selectedImageUriProfile.toString()
            )
            current.let { uid ->
                db.collection("profiles").document(uid).set(profile).addOnSuccessListener {
                    Toast.makeText(this, "تم حفظ البيانات بنجاح", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener {
                    Toast.makeText(
                        this, "عفوا حدث خطأ برجاء المحاولة مرة اخري", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(
                this,
                "لا يمكن الحصول على بيانات البريد الإلكتروني الخاصة بالمستخدم الحالي",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            selectedImageUri?.let { uri ->
                img.setImageURI(uri)
            }
        }
        if (requestCode == PICK_IMAGE_REQUESTProfile && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUriProfile = data.data?.toString()
            selectedImageUriProfile?.let { uriString ->
                Glide.with(this@PorfileActivity).load(Uri.parse(uriString)).into(img2)
            }
        }
    }


}
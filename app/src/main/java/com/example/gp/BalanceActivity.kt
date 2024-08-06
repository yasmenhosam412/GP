package com.example.gp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gp.Classes.ItemsViewModel
import com.example.gp.Classes.MyAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class BalanceActivity : AppCompatActivity() {
    private lateinit var btn: Button
    private lateinit var adapter: MyAdapter
    private lateinit var dataArray: ArrayList<ItemsViewModel>
    private lateinit var recyclerview: RecyclerView
    private lateinit var currentEmail: String
    private lateinit var currentPhone: String
    private lateinit var thisDate: String

    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    public lateinit var img: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance)

        initializeViews()
        setupRecyclerView()
        fetchCurrentUserDetails()
        loadItemsFromFirebase()

        findViewById<ImageView>(R.id.imageView6).setOnClickListener {
            refreshActivity()
        }

        findViewById<Button>(R.id.button17).setOnClickListener {
            showSearchDialog()
        }

        btn.setOnClickListener {
            showAddItemDialog()
        }
    }

    private fun initializeViews() {
        btn = findViewById(R.id.button16)
        recyclerview = findViewById(R.id.recycler)
    }

    private fun setupRecyclerView() {
        dataArray = ArrayList()
        adapter = MyAdapter(dataArray, this)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
    }

    private fun fetchCurrentUserDetails() {
        currentEmail = FirebaseAuth.getInstance().currentUser?.email.orEmpty()
        currentPhone = FirebaseAuth.getInstance().currentUser?.phoneNumber.orEmpty()
    }

    private fun refreshActivity() {
        startActivity(Intent(this, BalanceActivity::class.java))
        finish()
    }

    private fun showSearchDialog() {
        val dialog = AlertDialog.Builder(this)
        val searchView = SearchView(this)

        dialog.setView(searchView)
        dialog.setTitle("ابحث عن الوصف")
        dialog.setPositiveButton("بحث") { _, _ ->
            val query = searchView.query.toString()
            filterByDescription(query)
        }
        dialog.setNegativeButton("إلغاء", null)
        dialog.show()
    }

    private fun filterByDescription(description: String) {
        val filteredList = dataArray.filter { item ->
            item.description.contains(description, ignoreCase = true)
        }
        dataArray.clear()
        dataArray.addAll(filteredList)
        adapter.notifyDataSetChanged()
    }

    private fun showAddItemDialog() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
        }

        img = ImageView(this).apply {
            setImageResource(R.drawable.balance)
            id = View.generateViewId()
            layoutParams = ViewGroup.LayoutParams(500, 500)
            setOnClickListener {
                pickImage()
            }
        }

        layout.addView(img)

        val inputFields = arrayOf(
            createEditText("الوصف"),
            createEditText("السعر ل1 ك"),
            createEditText("الكمية"),
            createEditText("المحافظة"),
            createEditText("العنوان")
        )

        inputFields.forEach { layout.addView(it) }

        val dialog = AlertDialog.Builder(this)
            .setTitle("اضف محصول")
            .setMessage("تفاصيل المحصول").setCancelable(false)
            .setView(layout)
            .setPositiveButton("إضافة") { _, _ ->
                handleAddItem(inputFields, img)
            }
            .setNegativeButton("إلغاء", null)
            .create()

        dialog.show()
    }

    private fun createEditText(hint: String): EditText {
        return EditText(this).apply {
            this.hint = hint
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun handleAddItem(inputFields: Array<EditText>, img: ImageView) {
        val (description, price, amount, province, address) = inputFields.map { it.text.toString() }

        if (description.isNotEmpty() && price.isNotEmpty() && amount.isNotEmpty() &&
            province.isNotEmpty() && address.isNotEmpty() && selectedImageUri != null
        ) {

            val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale("ar")).format(Date())
            thisDate = currentDate

            val newItem = ItemsViewModel(
                selectedImageUri,
                thisDate,
                " الوصف :  $description",
                " السعر ل1 ك:  $price ج.م ",
                " الكمية : $amount",
                " المحافظة :  $province",
                "العنوان :  $address",
                currentEmail,
                currentPhone
            )

            dataArray.add(newItem)
            saveItemToFirebase(newItem)
            adapter.notifyDataSetChanged()
        } else {
            Toast.makeText(this, "عفوا يجب اضافة جميع تفاصيل المحصول", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveItemToFirebase(item: ItemsViewModel) {
        val database = FirebaseFirestore.getInstance()
        val itemMap = mapOf(
            "des" to item.description,
            "price" to item.price,
            "amount" to item.amount,
            "mohafza" to item.mohafza,
            "address" to item.address,
            "image" to item.image.toString(),
            "userEmail" to item.name,
            "userPhone" to item.phone
        )

        database.collection("items")
            .add(itemMap)
            .addOnSuccessListener {
                Toast.makeText(this, "تم حفظ البيانات بنجاح", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "فشلت محاولة حفظ البيانات", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadItemsFromFirebase() {
        val db = FirebaseFirestore.getInstance()
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale("ar"))
        thisDate = currentDate.format(Date())

        db.collection("items")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val des = document.getString("des")
                    val price = document.getString("price")
                    val amount = document.getString("amount")
                    val mohafza = document.getString("mohafza")
                    val address = document.getString("address")
                    val user = document.getString("userEmail")
                    val phone = document.getString("userPhone")
                    val imageUri = document.getString("image")?.let { Uri.parse(it) }

                    dataArray.add(
                        ItemsViewModel(
                            imageUri,
                            thisDate,
                            "$des",
                            " $price",
                            "$amount",
                            "$mohafza",
                            "$address",
                            user.orEmpty(),
                            phone.orEmpty()
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "فشلت محاولة تحميل البيانات", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            selectedImageUri?.let {
                img.setImageURI(it)
            }
        }
    }
}

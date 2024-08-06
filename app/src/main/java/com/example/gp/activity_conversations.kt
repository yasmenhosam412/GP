package com.example.gp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class activity_conversations : AppCompatActivity() {

    private lateinit var databaseRef: DatabaseReference
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var listViewUsers: ListView
    private lateinit var search: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations)

        search = findViewById(R.id.button14)
        listViewUsers = findViewById(R.id.list)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        listViewUsers.adapter = adapter

        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
        val currentUserPhone = FirebaseAuth.getInstance().currentUser?.phoneNumber ?: ""

        fetchAllUsers(currentUserEmail, currentUserPhone)

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchUsersByEmailAndPhone(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchUsersByEmailAndPhone(it) }
                return false
            }
        })

        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            val currentUser = auth.currentUser
            currentUser?.let {
                fetchPhoneNumberAndAddToList(it)
                fetchUserEmailAndAddToList(it)
            }
        }

        listViewUsers.setOnItemClickListener { _, _, position, _ ->
            val userDetails = adapter.getItem(position)
            userDetails?.let {
                openChatActivity(userDetails, FirebaseAuth.getInstance().currentUser?.uid ?: "")
            }
        }
    }

    private fun searchUsersByEmailAndPhone(emailPhone: String) {
        adapter.filter.filter(emailPhone)
    }

    private fun fetchAllUsers(currentUserEmail: String, currentUserPhone: String) {
        databaseRef = FirebaseDatabase.getInstance().getReference("users")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userList = ArrayList<String>()
                for (userSnapshot in dataSnapshot.children) {
                    val email = userSnapshot.child("email").getValue(String::class.java)
                    val phoneNumber = userSnapshot.child("phoneNumber").getValue(String::class.java)
                    email?.let {
                        if (it.isNotEmpty() && it != currentUserEmail) {
                            userList.add(it)
                        }
                    }
                    phoneNumber?.let {
                        if (it.isNotEmpty() && it != currentUserPhone) {
                            userList.add(it)
                        }
                    }
                }
                adapter.clear()
                adapter.addAll(userList)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun fetchPhoneNumberAndAddToList(user: FirebaseUser) {
        val phoneNumber = user.phoneNumber
        phoneNumber?.let {
            addUserPhoneNumberToList(user, phoneNumber)
        }
    }

    private fun addUserPhoneNumberToList(user: FirebaseUser, phoneNumber: String) {
        databaseRef.child(user.uid).child("phoneNumber").setValue(phoneNumber)
            .addOnSuccessListener {}
            .addOnFailureListener {}
    }

    private fun fetchUserEmailAndAddToList(user: FirebaseUser) {
        val email = user.email
        email?.let {
            addEmailToList(user, email)
        }
    }

    private fun addEmailToList(user: FirebaseUser, email: String) {
        databaseRef.child(user.uid).child("email").setValue(email)
            .addOnSuccessListener {}
            .addOnFailureListener {}
    }

    private fun openChatActivity(userDetail: String, otherUserId: String) {
        val intent = Intent(this, activity_chat::class.java)
        if (userDetail.contains("@")) {
            intent.putExtra("userEmail", userDetail)
        } else {
            intent.putExtra("userPhone", userDetail)
        }
        intent.putExtra("otherUserId", otherUserId)
        startActivity(intent)
    }
}
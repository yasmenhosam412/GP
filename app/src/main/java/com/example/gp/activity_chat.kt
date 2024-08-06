package com.example.gp

import ChatAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gp.Classes.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class activity_chat : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var database: DatabaseReference

    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var otherUserEmail: String
    private lateinit var otherUserPhone: String
    private lateinit var currentUserEmail: String

    private lateinit var txt66: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        auth = FirebaseAuth.getInstance()


        currentUser = auth.currentUser!!



        otherUserEmail = intent.getStringExtra("userEmail") ?: ""
        otherUserPhone = intent.getStringExtra("userPhone") ?: ""
        currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""




        recyclerView = findViewById(R.id.lili)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ChatAdapter()
        recyclerView.adapter = adapter



        messageEditText = findViewById(R.id.editTextText9)
        sendButton = findViewById(R.id.imageButton)
        txt66 = findViewById(R.id.textView66)


        txt66.setOnClickListener {
            val intent = Intent(this, PorfileActivity::class.java)
            intent.putExtra("OTHER_USER_EMAIL", otherUserEmail)
            intent.putExtra("OTHER_USER_PHONE", otherUserPhone)
            startActivity(intent)
        }





        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                messageEditText.text.clear()
            }
        }


        database = FirebaseDatabase.getInstance().reference.child("messages")
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                message?.let {
                    if (isMessageForCurrentUser(it)) {
                        adapter.addMessage(it)
                        recyclerView.scrollToPosition(adapter.itemCount - 1)
                    }
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun isMessageForCurrentUser(message: Message): Boolean {
        return (message.senderEmail == currentUserEmail && message.receiverEmail == otherUserEmail) ||
                (message.senderEmail == otherUserEmail && message.receiverEmail == currentUserEmail)
    }


    private fun sendMessage(messageText: String) {
        val message = Message(messageText, currentUserEmail, otherUserEmail)
        val messageId = database.push().key
        messageId?.let {
            database.child(it).setValue(message)
        }
    }
}

package com.example.gp.Classes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gp.PorfileActivity
import com.example.gp.R
import com.google.firebase.firestore.FirebaseFirestore


class notiChatAdapter(

    private val mList: MutableList<notiItems> = mutableListOf(), private val context: Context

) :


    RecyclerView.Adapter<notiChatAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notifications, parent, false)


        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myItems = mList[position]


        holder.newsss.text = myItems.news
        holder.newsss2.text = myItems.news2.toString()
        holder.newsss3.text = myItems.news3.toString()
        holder.newsss4.text = myItems.news4.toString()
        holder.newsss5.text = myItems.news5.toString()
        holder.newsss6.text = myItems.news6.toString()
        holder.newsss7.text = myItems.news7.toString()

    }



    override fun getItemCount(): Int {
        return mList.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {


        val newsss: TextView = itemView.findViewById(R.id.textView55)
        val newsss2: TextView = itemView.findViewById(R.id.textView56)
        val newsss3: TextView = itemView.findViewById(R.id.textView57)
        val newsss4: TextView = itemView.findViewById(R.id.textView58)
        val newsss5: TextView = itemView.findViewById(R.id.textView59)
        val newsss6: TextView = itemView.findViewById(R.id.textView60)
        val newsss7: TextView = itemView.findViewById(R.id.textView61)


    }


}




package com.example.gp.Classes

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gp.R
import com.example.gp.activity_conversations


class MyAdapter(

    private val mList: MutableList<ItemsViewModel> = mutableListOf(), private val context: Context

) :


    RecyclerView.Adapter<MyAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rere, parent, false)


        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myItems = mList[position]


        holder.date.text = myItems.date
        holder.description.text = myItems.description
        holder.price.text = myItems.price
        holder.amount.text = myItems.amount
        holder.mohafza.text = myItems.mohafza
        holder.address.text = myItems.address
        holder.name.text = myItems.name
        holder.phone.text=myItems.phone



        holder.phone.setOnClickListener {
            var intent = Intent(holder.itemView.context,activity_conversations::class.java)
            holder.itemView.context.startActivity(intent)
            Toast.makeText(holder.itemView.context, "${holder.phone.text}", Toast.LENGTH_SHORT).show()
        }

        holder.name.setOnClickListener {

            var intent = Intent(holder.itemView.context,activity_conversations::class.java)
            holder.itemView.context.startActivity(intent)
            Toast.makeText(holder.itemView.context, "${holder.name.text}", Toast.LENGTH_SHORT).show()


        }


        myItems.image?.let { uri ->
            Glide.with(holder.itemView)
                .load(uri)
                .into(holder.imageView)

        }
    }


    override fun getItemCount(): Int {
        return mList.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val imageView: ImageView = itemView.findViewById(R.id.imageView25)
        val date: TextView = itemView.findViewById(R.id.textView55)
        val description: TextView = itemView.findViewById(R.id.textView80)
        val price: TextView = itemView.findViewById(R.id.textView102)
        val amount: TextView = itemView.findViewById(R.id.textView103)
        val mohafza: TextView = itemView.findViewById(R.id.textView104)
        val address: TextView = itemView.findViewById(R.id.textView83)
        val name: TextView = itemView.findViewById(R.id.textView8)
        val phone: TextView = itemView.findViewById(R.id.textView9)


    }


}




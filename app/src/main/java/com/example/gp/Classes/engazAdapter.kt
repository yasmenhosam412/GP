package com.example.gp.Classes

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gp.R

class engazAdapter(private val engazzList: List<angazatItems>, private val context: Context) :
    RecyclerView.Adapter<engazAdapter.EngazzViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EngazzViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.engaz, parent, false)
        return EngazzViewHolder(view)
    }

    override fun onBindViewHolder(holder: EngazzViewHolder, position: Int) {
        val engazz = engazzList[position]
        holder.bind(engazz)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }
    }

    override fun getItemCount() = engazzList.size

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class EngazzViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView22)
        private val engazTextView: TextView = itemView.findViewById(R.id.textView32)

        fun bind(engazz: angazatItems) {
            engazTextView.text = engazz.engaz
            engazz.image?.let {
                Glide.with(itemView.context).load(it).into(imageView)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}

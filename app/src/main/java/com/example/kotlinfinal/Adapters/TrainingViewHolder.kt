package com.example.kotlinfinal.Adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinfinal.Model.Training
import com.example.kotlinfinal.R
import com.squareup.picasso.Picasso

class TrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val typeTextView: TextView = itemView.findViewById(R.id.tvTypeRow)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescriptionRow)
    private val imageView: ImageView = itemView.findViewById(R.id.imageView2)

    fun bind(training: Training) {
        typeTextView.text = training.type
        descriptionTextView.text = training.description

        // Load image using Picasso
        if (training.imgUrl.isNotEmpty()) {
            Picasso.get()
                .load(training.imgUrl)
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.avatar)
        }
    }
}



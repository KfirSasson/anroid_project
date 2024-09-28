

package com.example.kotlinfinal.Adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinfinal.Model.Training
import com.example.kotlinfinal.R

class TrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val typeTextView: TextView = itemView.findViewById(R.id.tvTypeRow)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescriptionRow)

    fun bind(training: Training) {
        typeTextView.text = training.type
        descriptionTextView.text = training.description
    }
}


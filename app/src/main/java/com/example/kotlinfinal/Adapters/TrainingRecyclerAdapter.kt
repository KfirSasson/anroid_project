package com.example.kotlinfinal.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinfinal.Model.Training
import com.example.kotlinfinal.R

class TrainingRecyclerAdapter(var trainings: MutableList<Training>?): RecyclerView.Adapter<TrainingViewHolder>() {

    override fun getItemCount(): Int = trainings?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.training_layout_row, parent, false)
        return TrainingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        val training = trainings?.get(position)
        holder.bind(training, position)
    }
}

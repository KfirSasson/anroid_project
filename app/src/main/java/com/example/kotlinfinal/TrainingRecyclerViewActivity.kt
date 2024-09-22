package com.example.kotlinfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinfinal.Model.Model
import com.example.kotlinfinal.Model.Training

class TrainingRecyclerViewActivity : AppCompatActivity() {

    var trainingRecyclerView: RecyclerView? = null
    var trainings: MutableList<Training>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_resycler_view)

        trainings = Model.instance.trainings
        trainingRecyclerView = findViewById(R.id.rvTrainingRecyclerList)
        trainingRecyclerView?.setHasFixedSize(true)

        trainingRecyclerView?.layoutManager = LinearLayoutManager(this)

        trainingRecyclerView?.adapter = TrainingRecyclerAdapter()
    }
    inner class TrainingViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView){

        var typeTextView: TextView? = null
        var descriptionTextView: TextView? = null
        init {
            typeTextView = itemView.findViewById(R.id.tvTypeRow)
            descriptionTextView = itemView.findViewById(R.id.tvDescriptionRow)
        }

        fun bind(training: Training?, position: Int) {
            typeTextView?.text = training?.type
            descriptionTextView?.text = training?.description
        }
    }
    inner class TrainingRecyclerAdapter: RecyclerView.Adapter<TrainingViewHolder>(){
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
}
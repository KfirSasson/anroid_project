package com.example.kotlinfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinfinal.Model.Model
import com.example.kotlinfinal.Model.Training

class TrainingListActivity : AppCompatActivity() {

    var trainingListView: ListView? = null
    var trainings: MutableList<Training>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_list)

        trainings = Model.instance.trainings

        trainingListView = findViewById(R.id.lvTrainingList)
        trainingListView?.adapter = TrainingListAdapter(trainings)
    }

    class TrainingListAdapter(val trainings: MutableList<Training>?): BaseAdapter() {
        override fun getCount(): Int = trainings?.size ?: 0

        override fun getItem(position: Int): Any {
            TODO("Not yet implemented")
        }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val training = trainings?.get(position)
            var view: View? = null
            if (convertView == null) {
                view = LayoutInflater.from(parent?.context).inflate(R.layout.training_layout_row, parent, false)
            }

            view = view ?: convertView

            val typeTextView: TextView? = view?.findViewById(R.id.tvTypeRow)
            val descriptionTextView: TextView? = view?.findViewById(R.id.tvDescriptionRow)

            typeTextView?.text = training?.type
            descriptionTextView?.text = training?.description

            return view!!
        }
        override fun getItemId(position: Int): Long {
            TODO("Not yet implemented")
        }
    }
}
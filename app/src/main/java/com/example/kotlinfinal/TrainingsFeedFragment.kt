package com.example.kotlinfinal

import android.os.Bundle
//import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinfinal.Adapters.TrainingRecyclerAdapter
import com.example.kotlinfinal.Model.Model
import com.example.kotlinfinal.Model.Training


class TrainingsFeedFragment : Fragment() {

    var trainingRecyclerView: RecyclerView? = null
    var trainings: MutableList<Training>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feed_trainings, container, false)

        trainings = Model.instance.trainings
        trainingRecyclerView = view.findViewById(R.id.rvTrainingsFrafmentList)
        trainingRecyclerView?.setHasFixedSize(true)
        trainingRecyclerView?.layoutManager = LinearLayoutManager(context)

        val adapter = TrainingRecyclerAdapter(trainings)
        trainingRecyclerView?.adapter = adapter

        return view
    }
    }
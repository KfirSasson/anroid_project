/*package com.example.kotlinfinal

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
    }*/

// TrainingsFeedFragment.kt

package com.example.kotlinfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinfinal.Adapters.TrainingRecyclerAdapter
import com.example.kotlinfinal.Model.Training
import com.google.firebase.firestore.FirebaseFirestore

class TrainingsFeedFragment : Fragment() {

    private lateinit var trainingRecyclerView: RecyclerView
    private lateinit var trainings: MutableList<Training>
    private lateinit var adapter: TrainingRecyclerAdapter
    private val db = FirebaseFirestore.getInstance() // Firestore instance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feed_trainings, container, false)

        // Initialize trainings list and RecyclerView
        trainings = mutableListOf()
        trainingRecyclerView = view.findViewById(R.id.rvTrainingsFrafmentList)
        trainingRecyclerView.setHasFixedSize(true)
        trainingRecyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize adapter and set it to RecyclerView
        adapter = TrainingRecyclerAdapter(trainings)
        trainingRecyclerView.adapter = adapter

        // Fetch trainings from Firestore
        fetchTrainingsFromFirestore()

        return view
    }

    private fun fetchTrainingsFromFirestore() {
        db.collection("trainings")
            .get()
            .addOnSuccessListener { documents ->
                trainings.clear()
                for (document in documents) {
                    val training = document.toObject(Training::class.java)
                    trainings.add(training)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error getting trainings: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

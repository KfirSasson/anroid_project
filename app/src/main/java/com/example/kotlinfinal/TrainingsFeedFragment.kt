package com.example.kotlinfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
        val view = inflater.inflate(R.layout.fragment_feed_trainings, container, false)

        trainings = mutableListOf()
        trainingRecyclerView = view.findViewById(R.id.rvTrainingsFrafmentList)
        trainingRecyclerView.setHasFixedSize(true)
        trainingRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter = TrainingRecyclerAdapter(trainings)
        trainingRecyclerView.adapter = adapter

        fetchTrainingsFromFirestore()

        val addButton: Button = view.findViewById(R.id.btnToAddTraining)
        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_trainingsFeedFragment_to_addTrainingFragment)
        }

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

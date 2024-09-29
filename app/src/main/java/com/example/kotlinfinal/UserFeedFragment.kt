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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserFeedFragment : Fragment() {

    private lateinit var trainingRecyclerView: RecyclerView
    private lateinit var userTrainings: MutableList<Training>
    private val db = FirebaseFirestore.getInstance() // Firestore instance
    private val auth = FirebaseAuth.getInstance() // FirebaseAuth instance
    private lateinit var adapter: TrainingRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_feed, container, false)

        userTrainings = mutableListOf()
        trainingRecyclerView = view.findViewById(R.id.rvUserFeedList)
        trainingRecyclerView.setHasFixedSize(true)
        trainingRecyclerView.layoutManager = LinearLayoutManager(context)

        adapter = TrainingRecyclerAdapter(userTrainings)
        trainingRecyclerView.adapter = adapter

        fetchUserTrainings()

        return view
    }

    private fun fetchUserTrainings() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            db.collection("trainings")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    userTrainings.clear()
                    for (document in documents) {
                        val training = document.toObject(Training::class.java)
                        userTrainings.add(training)
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error fetching trainings: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "You must be logged in to view your feed", Toast.LENGTH_SHORT).show()
        }
    }
}


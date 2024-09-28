/*package com.example.kotlinfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinfinal.Adapters.TrainingRecyclerAdapter
import com.example.kotlinfinal.Model.Model
import com.example.kotlinfinal.Model.Training

class UserFeedFragment : Fragment() {

    private var trainingRecyclerView: RecyclerView? = null
    private var userTrainings: MutableList<Training>? = null
    private val loggedInUserId = "currentUserId" // Replace with actual user ID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_feed, container, false)

        // Filter posts to show only those uploaded by the logged-in user
        /*userTrainings = Model.instance.trainings.filter { it.userId == loggedInUserId }.toMutableList()*/

        trainingRecyclerView = view.findViewById(R.id.rvUserFeedList)
        trainingRecyclerView?.setHasFixedSize(true)
        trainingRecyclerView?.layoutManager = LinearLayoutManager(context)

        val adapter = userTrainings?.let { TrainingRecyclerAdapter(it) }
        trainingRecyclerView?.adapter = adapter

        return view
    }
}*/

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_feed, container, false)

        // Initialize the RecyclerView and other components
        userTrainings = mutableListOf()
        trainingRecyclerView = view.findViewById(R.id.rvUserFeedList)
        trainingRecyclerView.setHasFixedSize(true)
        trainingRecyclerView.layoutManager = LinearLayoutManager(context)

        // Set up the adapter
        adapter = TrainingRecyclerAdapter(userTrainings)
        trainingRecyclerView.adapter = adapter

        // Fetch user-specific trainings
        fetchUserTrainings()

        return view
    }

    private fun fetchUserTrainings() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            // Query Firestore for trainings uploaded by the current user
            db.collection("trainings")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    userTrainings.clear() // Clear the previous list
                    for (document in documents) {
                        val training = document.toObject(Training::class.java)
                        userTrainings.add(training)
                    }
                    adapter.notifyDataSetChanged() // Notify the adapter to refresh the list
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error fetching trainings: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Handle the case where the user is not logged in
            Toast.makeText(context, "You must be logged in to view your feed", Toast.LENGTH_SHORT).show()
        }
    }
}


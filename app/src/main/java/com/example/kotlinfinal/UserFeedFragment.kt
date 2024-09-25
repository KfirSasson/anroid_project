package com.example.kotlinfinal

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

        val adapter = TrainingRecyclerAdapter(userTrainings)
        trainingRecyclerView?.adapter = adapter

        return view
    }
}

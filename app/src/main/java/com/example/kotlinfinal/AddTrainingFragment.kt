package com.example.kotlinfinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.kotlinfinal.Model.Training
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTrainingFragment : Fragment() {

    private var typeTextField: EditText? = null
    private var descriptionTextField: EditText? = null
    private var saveButton: Button? = null
    private var cancelButton: Button? = null
    private val db = FirebaseFirestore.getInstance() // Firestore database instance
    private val auth = FirebaseAuth.getInstance() // Firebase Auth instance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_training, container, false)
        setupUI(view)
        return view
    }

    private fun setupUI(view: View) {
        typeTextField = view.findViewById(R.id.etAddTrainingType)
        descriptionTextField = view.findViewById(R.id.etAddTrainingDecription)
        saveButton = view.findViewById(R.id.btnAddTrainingSave)
        cancelButton = view.findViewById(R.id.btnaddTrainingCancel)

        saveButton?.setOnClickListener {
            val type = typeTextField?.text.toString().trim()
            val description = descriptionTextField?.text.toString().trim()

            if (type.isNotEmpty() && description.isNotEmpty()) {
                val newTraining = Training(0, type, description, "defaultImage")

                // Ensure user is authenticated before saving
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    // Attach the userId to the training object
                    newTraining.userId = currentUser.uid
                    // Save to Firestore first
                    saveTrainingToFirestore(newTraining)
                } else {
                    Toast.makeText(requireContext(), "You must be signed in to add a training", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton?.setOnClickListener {
            // Handle cancel action if necessary
        }
    }

    private fun saveTrainingToFirestore(training: Training) {
        // Save the training data to Firestore
        db.collection("trainings")
            .add(training)
            .addOnSuccessListener { documentReference ->
                // Do not assign hashCode() to trainingId, let Room handle it
               saveTrainingToLocalDatabase(training)
                Toast.makeText(requireContext(), "Training added successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to add training: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

   private fun saveTrainingToLocalDatabase(training: Training) {
        // Save the training data to the local Room database
        val trainingDao = AppLocalDb.getDatabase(requireContext()).trainingDao()
        CoroutineScope(Dispatchers.IO).launch {
            trainingDao.insertTraining(training)
        }
    }
}

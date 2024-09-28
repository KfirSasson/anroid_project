/*package com.example.kotlinfinal

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
}*/


package com.example.kotlinfinal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.kotlinfinal.Model.Training
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*

class AddTrainingFragment : Fragment() {

    private var typeTextField: EditText? = null
    private var descriptionTextField: EditText? = null
    private var saveButton: Button? = null
    private var cancelButton: Button? = null
    private var selectImageButton: Button? = null
    private var selectedImageView: ImageView? = null

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storageRef = FirebaseStorage.getInstance().reference

    private var selectedImageUri: Uri? = null

    private val PICK_IMAGE_REQUEST = 1

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
        selectImageButton = view.findViewById(R.id.btnSelectImage)
        selectedImageView = view.findViewById(R.id.ivSelectedImage)

        selectImageButton?.setOnClickListener {
            openImageChooser()
        }

        saveButton?.setOnClickListener {
            val type = typeTextField?.text.toString().trim()
            val description = descriptionTextField?.text.toString().trim()

            if (type.isNotEmpty() && description.isNotEmpty()) {
                // Ensure user is authenticated before saving
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    // Handle image upload if an image is selected
                    if (selectedImageUri != null) {
                        uploadImageToStorage { imageUrl ->
                            // After image is uploaded, save training with image URL
                            saveTraining(type, description, imageUrl)
                        }
                    } else {
                        // No image selected, save training without image URL
                        saveTraining(type, description, "")
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "You must be signed in to add a training",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        cancelButton?.setOnClickListener {
            // Handle cancel action if necessary
        }
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICK_IMAGE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                    selectedImageUri = data.data
                    selectedImageView?.visibility = View.VISIBLE
                    selectedImageView?.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun uploadImageToStorage(callback: (String) -> Unit) {
        val currentUser = auth.currentUser ?: return
        val imageRef = storageRef.child("trainings/${System.currentTimeMillis()}_${currentUser.uid}.jpg")

        selectedImageUri?.let { uri ->
            val uploadTask = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    callback(downloadUri.toString())
                }.addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to get image URL: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveTraining(type: String, description: String, imageUrl: String) {
        val currentUser = auth.currentUser ?: return
        val newTraining = Training(
            type = type,
            description = description,
            imgUrl = imageUrl,
            userId = currentUser.uid
        )

        // Save to Firestore
        db.collection("trainings")
            .add(newTraining)
            .addOnSuccessListener { documentReference ->
                // Save to Room database
                saveTrainingToLocalDatabase(newTraining)
                Toast.makeText(requireContext(), "Training added successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to add training: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveTrainingToLocalDatabase(training: Training) {
        val trainingDao = AppLocalDb.getDatabase(requireContext()).trainingDao()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                trainingDao.insertTraining(training)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Training saved locally!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to save training locally: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }
        }
    }
}

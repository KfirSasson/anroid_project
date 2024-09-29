package com.example.kotlinfinal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    if (selectedImageUri != null) {
                        uploadImageToStorage { imageUrl ->
                            saveTraining(type, description, imageUrl)
                            findNavController().navigate(R.id.action_addTrainingFragment_to_trainingsFeedFragment)
                        }
                    } else {
                        saveTraining(type, description, "")
                        findNavController().navigate(R.id.action_addTrainingFragment_to_trainingsFeedFragment)
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
            findNavController().navigate(R.id.action_addTrainingFragment_to_trainingsFeedFragment)
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

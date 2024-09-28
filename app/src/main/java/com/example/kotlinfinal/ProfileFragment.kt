package com.example.kotlinfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var etProfileName: EditText
    private lateinit var etProfileEmail: EditText
    private lateinit var btnSaveProfile: Button
    private lateinit var btnLogout: Button

    private val auth = FirebaseAuth.getInstance() // Firebase Auth instance
    private val db = FirebaseFirestore.getInstance() // Firestore instance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize the views
        etProfileName = view.findViewById(R.id.etProfileName)
        etProfileEmail = view.findViewById(R.id.etProfileEmail)
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile)
        btnLogout = view.findViewById(R.id.btnLogout)

        // Load current user profile data
        loadUserProfile()

        // Set up save button click listener
        btnSaveProfile.setOnClickListener {
            saveUserProfile()
        }

        // Set up logout button click listener
        btnLogout.setOnClickListener {
            logOutUser()
        }

        return view
    }

    // Load user profile from Firestore and Firebase Auth
    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Set email directly from FirebaseAuth
            etProfileEmail.setText(currentUser.email)

            // Fetch additional user details from Firestore (e.g., username)
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("userName") ?: ""
                        etProfileName.setText(name)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error loading profile", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    // Save updated user profile to Firebase Auth and Firestore
    private fun saveUserProfile() {
        val currentUser = auth.currentUser
        val name = etProfileName.text.toString()
        val email = etProfileEmail.text.toString()

        if (currentUser != null) {
            // Update FirebaseAuth profile (name and email)
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Update email if it has changed
                        if (email != currentUser.email) {
                            currentUser.updateEmail(email)
                                .addOnCompleteListener { emailTask ->
                                    if (emailTask.isSuccessful) {
                                        // Update Firestore with new data
                                        saveUserToFirestore(currentUser.uid, name, email)
                                    } else {
                                        Toast.makeText(context, "Failed to update email", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            // No email change, just update Firestore
                            saveUserToFirestore(currentUser.uid, name, email)
                        }
                    } else {
                        Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    // Save user data to Firestore
    private fun saveUserToFirestore(userId: String, name: String, email: String) {
        val user = hashMapOf(
            "userName" to name,
            "userEmail" to email
        )

        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to update profile in Firestore", Toast.LENGTH_SHORT).show()
            }
    }

    // Log the user out of Firebase and navigate to the login screen
    private fun logOutUser() {
        auth.signOut() // Sign the user out

        // Redirect the user to the LoginFragment (instead of LoginActivity)
        findNavController().navigate(R.id.action_profileFragment_to_loginFragment) // Ensure this navigation action exists
    }
}

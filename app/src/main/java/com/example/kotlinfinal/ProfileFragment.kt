package com.example.kotlinfinal

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var etProfileName: EditText
    private lateinit var etProfileEmail: EditText
    private lateinit var tvProfileName: TextView
    private lateinit var btnSaveProfile: Button
    private lateinit var btnLogout: Button

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        etProfileName = view.findViewById(R.id.etProfileName)
        etProfileEmail = view.findViewById(R.id.etProfileEmail)
        tvProfileName = view.findViewById(R.id.tvProfileName)
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile)
        btnLogout = view.findViewById(R.id.btnLogout)

        tvProfileName.text = "Loading..."

        loadUserProfile()

        etProfileName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tvProfileName.text = s.toString()
            }
        })

        btnSaveProfile.setOnClickListener {
            saveUserProfile()
        }

        btnLogout.setOnClickListener {
            logOutUser()
        }

        return view
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            etProfileEmail.setText(currentUser.email)

            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("userName") ?: "Unknown"
                        etProfileName.setText(name)
                        tvProfileName.text = name
                    } else {
                        tvProfileName.text = "User's Name"
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error loading profile", Toast.LENGTH_SHORT).show()
                    tvProfileName.text = "User's Name"
                }
        } else {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserProfile() {
        val currentUser = auth.currentUser
        val name = etProfileName.text.toString()
        val email = etProfileEmail.text.toString()

        if (currentUser != null) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (email != currentUser.email) {
                            currentUser.updateEmail(email)
                                .addOnCompleteListener { emailTask ->
                                    if (emailTask.isSuccessful) {
                                        saveUserToFirestore(currentUser.uid, name, email)
                                    } else {
                                        Toast.makeText(context, "Failed to update email", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            saveUserToFirestore(currentUser.uid, name, email)
                        }
                    } else {
                        Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

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

    private fun logOutUser() {
        auth.signOut()
        findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
    }
}

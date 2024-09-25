package com.example.kotlinfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide the BottomNavigationView when the LoginFragment is displayed
        val bottomNav: BottomNavigationView? = activity?.findViewById(R.id.bnvNavigation)
        bottomNav?.visibility = View.GONE

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        val loginButton: Button = view.findViewById(R.id.btnLogin)
        val signUpButton: Button = view.findViewById(R.id.btnSignUp)
        val emailEditText: EditText = view.findViewById(R.id.etEmail)
        val passwordEditText: EditText = view.findViewById(R.id.etPassword)

        // Handle login button click
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateInput(email, password)) {
                loginUser(email, password)
            }
        }

        // Handle sign up navigation
        signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        // Ensure BottomNavigationView is hidden when the fragment is resumed
        val bottomNav: BottomNavigationView? = activity?.findViewById(R.id.bnvNavigation)
        bottomNav?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Show the BottomNavigationView again when leaving the LoginFragment
        val bottomNav: BottomNavigationView? = activity?.findViewById(R.id.bnvNavigation)
        bottomNav?.visibility = View.VISIBLE
    }

    // Validate email and password input
    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(requireContext(), "Email is required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(requireContext(), "Password is required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    // Login the user with FirebaseAuth
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful, navigate to home or main fragment
                    Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                    findNavControllerSafely()?.navigate(R.id.action_loginFragment_to_trainingsFeedFragment)
                } else {
                    // Login failed, show error message
                    Toast.makeText(requireContext(), "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun findNavControllerSafely(): NavController? {
        return try {
            findNavController()
        } catch (e: IllegalStateException) {
            null
        }
    }
}

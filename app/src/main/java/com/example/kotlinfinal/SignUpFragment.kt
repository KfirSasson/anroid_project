package com.example.kotlinfinal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.kotlinfinal.Model.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userDao: UserDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val signUpButton: Button = view.findViewById(R.id.btnSignInFragment)
        val nameEditText: EditText = view.findViewById(R.id.etNameSignUpFragment)
        val emailEditText: EditText = view.findViewById(R.id.etEmailSignUpFragment)
        val passwordEditText: EditText = view.findViewById(R.id.etPasswordSignUpFragment)

        signUpButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Validate input fields
            if (validateInput(name, email, password)) {
                // Proceed to sign up the user if validation passes
                signUpUser(name, email, password)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val bottomNav: BottomNavigationView? = activity?.findViewById(R.id.bnvNavigation)
        bottomNav?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val bottomNav: BottomNavigationView? = activity?.findViewById(R.id.bnvNavigation)
        bottomNav?.visibility = View.VISIBLE
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Name is required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty()) {
            Toast.makeText(requireContext(), "Email is required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(requireContext(), "Password is required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(requireContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun signUpUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    firebaseUser?.let {
                        val newUser = User(
                            userId = firebaseUser.uid,
                            userName = name,
                            userEmail = email,
                            userPassword = password
                        )

                        // Save user info to Room Database
                        saveUserToLocalDatabase(newUser)

                        Toast.makeText(requireContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show()

                        findNavControllerSafely()?.navigate(R.id.action_signUpFragment_to_loginFragment)
                    }
                } else {
                    Toast.makeText(requireContext(), "Sign Up Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    Log.e("SignUpFragment", "Sign up failed", task.exception)
                }
            }
    }

    private fun saveUserToLocalDatabase(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                userDao.insertUser(user)
                withContext(Dispatchers.Main) {
                    Log.d("SignUpFragment", "User saved to Room database")
                }
            } catch (e: Exception) {
                Log.e("SignUpFragment", "Error saving user to Room database", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error saving user to database", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun findNavControllerSafely(): NavController? {
        return try {
            findNavController()
        } catch (e: IllegalStateException) {
            Log.e("SignUpFragment", "Error navigating: Fragment not attached", e)
            null
        }
    }
}

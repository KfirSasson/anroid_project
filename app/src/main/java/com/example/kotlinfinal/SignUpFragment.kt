package com.example.kotlinfinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController


class SignUpFragment : Fragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the button using findViewById
        val signUpButton: Button = view.findViewById(R.id.btnSignInFragment)

        // Set up the button click listener
        signUpButton.setOnClickListener {
            // Navigate to LoginFragment
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        return view
    }


}
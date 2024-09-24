package com.example.kotlinfinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText


class AddTrainingFragment : Fragment() {

    private var typeTextField: EditText? = null
    private var descriptionTextField: EditText? = null
    private var saveButton: Button? = null
    private var cancelButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_training, container, false)
        setupUI(view)
        return view
    }
    fun setupUI(view: View) {
        typeTextField = view.findViewById(R.id.etAddTrainingType)
        descriptionTextField = view.findViewById(R.id.etAddTrainingDecription)
        saveButton = view.findViewById(R.id.btnAddTrainingSave)
        cancelButton = view.findViewById(R.id.btnaddTrainingCancel)

        cancelButton?.setOnClickListener{
            //later
        }

    }

}
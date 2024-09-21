package com.example.kotlinfinal

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddTrainingActivity : AppCompatActivity() {

    var typeTextField: EditText? = null
    var descriptionTextField: EditText? = null
    var saveButton: Button? = null
    var cancelButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_training)

setupUI()
    }

    fun setupUI() {
        typeTextField = findViewById(R.id.etAddTrainingType)
        descriptionTextField = findViewById(R.id.etAddTrainingDecription)
        saveButton = findViewById(R.id.btnAddTrainingSave)
        cancelButton = findViewById(R.id.btnaddTrainingCancel)

        cancelButton?.setOnClickListener{
            finish()
        }

    }

}
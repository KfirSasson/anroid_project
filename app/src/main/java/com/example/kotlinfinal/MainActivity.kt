package com.example.kotlinfinal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.kotlinfinal.R
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Ensure you have activity_main layout with BottomNavigationView

        // Find the NavigationHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostMain) as NavHostFragment
        val navController = navHostFragment.navController

        // Set up the BottomNavigationView with NavController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bnvNavigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        FirebaseApp.initializeApp(this)

    }
}

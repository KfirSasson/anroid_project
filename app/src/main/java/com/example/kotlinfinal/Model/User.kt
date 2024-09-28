package com.example.kotlinfinal.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val userId: String,
    val userName: String,
    val userEmail: String,
    val userPassword: String
)
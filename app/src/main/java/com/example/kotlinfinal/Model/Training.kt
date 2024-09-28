package com.example.kotlinfinal.Model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Training(
    @PrimaryKey(autoGenerate = true) var trainingId: Int = 0,
    val type: String = "",
    val description: String = "",
    val imgUrl: String = "",
    var userId: String = ""
)

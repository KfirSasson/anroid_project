package com.example.kotlinfinal.Model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Training(
    @PrimaryKey(autoGenerate = true) val trainingId: Int = 0,
    val type : String,
    val description: String,
    val img: String
)
/*data class Training(val type: String,
                    val description: String,
                    val avatar: String,
                    val userId:String)*/
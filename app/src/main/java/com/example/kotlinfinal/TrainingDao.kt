package com.example.kotlinfinal


import androidx.room.*
import com.example.kotlinfinal.Model.Training

@Dao
interface TrainingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTraining(training: Training)

    @Update
    suspend fun updateTraining(training: Training)

    @Delete
    suspend fun deleteTraining(training: Training)

    @Query("SELECT * FROM Training")
    suspend fun getAllTrainings(): List<Training>
}

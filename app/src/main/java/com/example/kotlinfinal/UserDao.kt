package com.example.kotlinfinal

import androidx.room.*
import com.example.kotlinfinal.Model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM User WHERE userId = :id")
    suspend fun getUserById(id: String): User?

    @Query("SELECT * FROM User")
    suspend fun getAllUsers(): List<User>
}

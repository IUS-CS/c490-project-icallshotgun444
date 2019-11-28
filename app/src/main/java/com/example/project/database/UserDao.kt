package com.example.project.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.project.User
import java.util.*

@Dao
interface UserDao {

    @Query("select * from user")
    fun getUser(): LiveData<User>

    @Update
    fun updateUser(user: User)

    @Insert
    fun insertUser(user: User)

}
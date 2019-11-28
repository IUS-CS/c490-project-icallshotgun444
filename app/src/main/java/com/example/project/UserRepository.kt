package com.example.project

import android.content.Context
import androidx.room.Room
import com.example.project.database.UserDatabase
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "user-database"

class UserRepository private constructor(context: Context) {
    companion object {
        private var instance: UserRepository? = null

        fun initialize(context: Context) {
            if (instance == null) {
                instance = UserRepository(context)
            }
        }

        fun get(): UserRepository {
            return instance ?: throw IllegalStateException("User Repository must be initialized!")
        }
    }

    private val database: UserDatabase = Room.databaseBuilder(
        context.applicationContext,
        UserDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val userDao = database.userDao()
    private val executor = Executors.newSingleThreadExecutor()


    fun getUser() = userDao.getUser()

    fun updateUser(user: User) =
        executor.execute {
            userDao.updateUser(user)
        }

    fun insertUser(user: User) =
        executor.execute {
            userDao.insertUser(user)
        }

}
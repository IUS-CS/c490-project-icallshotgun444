package com.example.project.database

import androidx.room.TypeConverter
import java.util.*
import com.google.gson.Gson

class UserTypeConverters {


    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid.toString()
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun listToJson(value: List<Int>?): String {

        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<Int>? {

        val objects = Gson().fromJson(value, Array<Int>::class.java) as Array<Int>
        val list = objects.toList()
        return list
    }
}
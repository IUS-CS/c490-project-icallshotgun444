package com.example.project

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class User(@PrimaryKey var id: UUID = UUID.randomUUID(),
                 var likes: Int = 0,
                var followers: Int = 0,
                var platformLevels: List<Int> = emptyList()
)
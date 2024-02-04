package com.niloythings.kittask.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstName: String? =null,
    val lastName: String? =null,
    val age: Int? =null,
    val gender: String? =null,
    val address: String? =null,
    val occupation: String? =null,
    val imageUrl: ByteArray? =null // Store the path or URL of the image
)

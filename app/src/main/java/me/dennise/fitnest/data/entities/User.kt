package me.dennise.fitnest.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val password: String,
    val email: String,
    val gender: String,
    val mobile: String,
    val yearOfBirth: Int,
    val receiveUpdates: Boolean
)
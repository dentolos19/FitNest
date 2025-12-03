package me.dennise.fitnest.data.entities

import androidx.room.*

@Entity(
    tableName = "workouts",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["userId"])]
)
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val name: String,
    val category: String,
    val duration: Int?,
    val date: String?,
    val time: String?,
    val comments: String?,
    val enjoyment: String
)
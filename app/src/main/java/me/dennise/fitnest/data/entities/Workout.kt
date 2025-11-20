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

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts WHERE userId = :userId ORDER BY id DESC")
    suspend fun all(userId: Int): List<Workout>

    @Query("SELECT * FROM workouts WHERE id = :id LIMIT 1")
    suspend fun get(id: Int): Workout?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: Workout)

    @Delete
    suspend fun delete(workout: Workout)
}
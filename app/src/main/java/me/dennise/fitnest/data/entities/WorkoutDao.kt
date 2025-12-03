package me.dennise.fitnest.data.entities

import androidx.room.*

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
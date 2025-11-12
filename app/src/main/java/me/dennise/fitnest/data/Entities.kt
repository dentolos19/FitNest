package me.dennise.fitnest.data

import androidx.room.*

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

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun get(username: String): User?

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun getWithPassword(username: String, password: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun updateUser(user: User)
}

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
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
    @Query("SELECT * FROM workouts ORDER BY id DESC")
    suspend fun all(): List<Workout>

    @Query("SELECT * FROM workouts WHERE id = :id LIMIT 1")
    suspend fun get(id: Int): Workout?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: Workout)

    @Delete
    suspend fun delete(workout: Workout)
}


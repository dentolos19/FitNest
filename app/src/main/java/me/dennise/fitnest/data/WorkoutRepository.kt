package me.dennise.fitnest.data

import me.dennise.fitnest.data.entities.Workout
import me.dennise.fitnest.data.entities.WorkoutDao

class WorkoutRepository(private val workoutDao: WorkoutDao) {
    suspend fun getWorkouts(userId: Int) = workoutDao.all(userId)
    suspend fun getWorkout(id: Int) = workoutDao.get(id)
    suspend fun addWorkout(workout: Workout) = workoutDao.insert(workout)
    suspend fun deleteWorkout(workout: Workout) = workoutDao.delete(workout)
}
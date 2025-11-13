package me.dennise.fitnest.data

class UserRepository(private val userDao: UserDao) {
    suspend fun loginUser(username: String, password: String) = userDao.getWithPassword(username, password)
    suspend fun registerUser(user: User) = userDao.insert(user)
    suspend fun getUser(username: String) = userDao.get(username)
}

class WorkoutRepository(private val workoutDao: WorkoutDao) {
    suspend fun getWorkouts(userId: Int) = workoutDao.all(userId)
    suspend fun getWorkout(id: Int) = workoutDao.get(id)
    suspend fun addWorkout(workout: Workout) = workoutDao.insert(workout)
    suspend fun deleteWorkout(workout: Workout) = workoutDao.delete(workout)
}
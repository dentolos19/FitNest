package me.dennise.fitnest.data

import me.dennise.fitnest.data.entities.User
import me.dennise.fitnest.data.entities.UserDao

class UserRepository(private val userDao: UserDao) {
    suspend fun loginUser(username: String, password: String) = userDao.getWithPassword(username, password)
    suspend fun registerUser(user: User) = userDao.insert(user)
    suspend fun getUser(username: String) = userDao.get(username)
    suspend fun updateUser(user: User) = userDao.update(user)
}
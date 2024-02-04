package com.niloythings.kittask.repository

import androidx.lifecycle.LiveData
import com.niloythings.kittask.dao.UserDao
import com.niloythings.kittask.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    fun getAllUsers(): LiveData<List<User>> {
        return userDao.getAllUsersLiveData()
    }

    suspend fun getUserById(userId: Int): User {
        return withContext(Dispatchers.IO) {
            userDao.getUserById(userId)
        }
    }
}
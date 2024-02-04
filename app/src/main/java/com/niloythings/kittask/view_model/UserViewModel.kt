package com.niloythings.kittask.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.niloythings.kittask.dao.UserDao
import com.niloythings.kittask.entities.User
import com.niloythings.kittask.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun registerUser(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }

    val userList: LiveData<List<User>> = userRepository.getAllUsers()

    suspend fun getUserById(userId: Int): User {
        return userRepository.getUserById(userId)
    }

}


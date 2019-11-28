package com.example.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class PlatformListViewModel : ViewModel() {
    private val userRepository = UserRepository.get()
    val user: LiveData<User> = userRepository.getUser()
}

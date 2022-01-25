package com.example.e_queue.app.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_queue.app.data.model.User
import com.example.e_queue.app.domain.repository.EQueueRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EQueueViewModel constructor(private val eQueueRepository: EQueueRepository) : ViewModel() {
    private val _user = MutableLiveData<List<User>>()
    val user: LiveData<List<User>> = _user



    var userList: List<User> = listOf()

    fun getUserList() {
        viewModelScope.launch(Dispatchers.IO){
            userList = eQueueRepository.getUsers()
            _user.postValue(userList)
        }
    }

}
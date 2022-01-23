package com.example.e_queue.app.presentation.viewModel

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_queue.app.data.model.User
import com.example.e_queue.app.domain.repository.EQueueRepository
import com.example.e_queue.app.presentation.di.viewModelModules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    private var myJob: Job? = null

    fun test(name: String, userName: TextView) {
        myJob?.cancel()
        myJob = viewModelScope.launch(Dispatchers.IO){
            userName.text = name
        }
    }
}
package com.example.e_queue.app.presentation.viewModel

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.app.data.model.UserServiceLength
import com.example.e_queue.app.domain.repository.EQueueRepository
import com.example.e_queue.framework.remote.RemoteDataSource
import kotlinx.coroutines.*

class LoggedUserViewModel constructor(private val eQueueRepository: EQueueRepository,val loggedUserModel: LoggedUser) : ViewModel() {

    private var myJob: Job? = null
    var pause = false

    private val _loggedUser = MutableLiveData<LoggedUser>()
    val loggedUser: LiveData<LoggedUser> = _loggedUser

    private val _serviceLength = MutableLiveData<UserServiceLength>()
    val serviceLength: LiveData<UserServiceLength> = _serviceLength

    fun setUserParams() {
        _loggedUser.postValue(
            LoggedUser(
                id = loggedUserModel.id,
                point = loggedUserModel.point,
                name = loggedUserModel.name,
                service_id = loggedUserModel.service_id
            )
        )
    }

    private suspend fun getServiceLength(serviceId: Long?) {
        while (true) {
            if (!pause) {
                val length = eQueueRepository.getUserServiceLength(serviceId)
                Log.d("pidort", "Request ok!")
                _serviceLength.postValue(length)
            }
            delay(5000)
        }
    }

    fun test(serviceId: Long?) {
        myJob?.cancel()
        myJob = viewModelScope.launch(Dispatchers.IO) {
            getServiceLength(serviceId)
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getServiceLength(loggedUserModel.service_id)
        }
    }
}
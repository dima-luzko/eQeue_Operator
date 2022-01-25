package com.example.e_queue.app.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.app.data.model.UserServiceLength
import com.example.e_queue.app.domain.repository.EQueueRepository
import com.example.e_queue.utils.Constants.Companion.LOG_SERVICE_LENGTH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoggedUserViewModel constructor(
    private val eQueueRepository: EQueueRepository,
    val loggedUserModel: LoggedUser
) : ViewModel() {

    private var myJob: Job? = null

    private val _loggedUser = MutableLiveData<LoggedUser>()
    val loggedUser: LiveData<LoggedUser> = _loggedUser

    private val _serviceLength = MutableLiveData<UserServiceLength>()
    val serviceLength: LiveData<UserServiceLength> = _serviceLength

    fun setUserParams() {
        _loggedUser.postValue(loggedUserModel)
    }

    private suspend fun getServiceLength(serviceId: Long?) {
        while (true) {
            val length = eQueueRepository.getUserServiceLength(serviceId)
            _serviceLength.postValue(length)
            Log.d(
                LOG_SERVICE_LENGTH,
                "Now length in service - ${length.length} for user: ${loggedUserModel.name}"
            )
            delay(5000)
        }
    }

    fun startGetServiceLength() {
        myJob = viewModelScope.launch(Dispatchers.IO) {
            getServiceLength(loggedUserModel.service_id)
        }
        Log.d(LOG_SERVICE_LENGTH, "START get service length")
    }

    fun stopGetServiceLength() {
        myJob?.cancel()
        Log.d(LOG_SERVICE_LENGTH, "STOP get service length")
    }
}
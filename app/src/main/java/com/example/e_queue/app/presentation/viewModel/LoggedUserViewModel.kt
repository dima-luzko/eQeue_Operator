package com.example.e_queue.app.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.app.data.model.NextCustomerInfo
import com.example.e_queue.app.data.model.UserServiceLength
import com.example.e_queue.app.domain.repository.EQueueRepository
import com.example.e_queue.utils.Constants.Companion.LOG_NEXT_CUSTOMER_INFO
import com.example.e_queue.utils.Constants.Companion.LOG_SERVICE_LENGTH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoggedUserViewModel constructor(
    private val eQueueRepository: EQueueRepository,
    val loggedUserModel: LoggedUser
) : ViewModel() {

    private var jobForServiceLength: Job? = null
    private var jobForNextCustomerInfo: Job? = null

    private val _loggedUser = MutableLiveData<LoggedUser>()
    val loggedUser: LiveData<LoggedUser> = _loggedUser

    private val _serviceLength = MutableLiveData<UserServiceLength>()
    val serviceLength: LiveData<UserServiceLength> = _serviceLength

    private val _nextCustomerInfo = MutableLiveData<NextCustomerInfo>()
    val nextCustomerInfo: LiveData<NextCustomerInfo> = _nextCustomerInfo

    fun setUserParams() {
        _loggedUser.postValue(loggedUserModel)
    }

    private suspend fun getServiceLength() {
        while (true) {
            val length = eQueueRepository.getUserServiceLength(loggedUserModel.serviceId)
            _serviceLength.postValue(length)
            Log.d(
                LOG_SERVICE_LENGTH,
                "Now length in service - ${length.length} for user: ${loggedUserModel.name}"
            )
            delay(5000)
        }
    }

    private suspend fun getNextCustomerInfo() {
        while (true) {
            val customer = eQueueRepository.getNextCustomerInfo(loggedUserModel.id)
            _nextCustomerInfo.postValue(customer)
            Log.d(
                LOG_NEXT_CUSTOMER_INFO,
                "Next customer - ${customer.prefix}${customer.number} "
            )
            delay(5000)
        }
    }

    fun startGetNextCustomerInfo() {
        jobForNextCustomerInfo = viewModelScope.launch(Dispatchers.IO) {
            runCatching { getNextCustomerInfo() }
        }
    }

    fun stopGetNextCustomerInfo() {
        jobForNextCustomerInfo?.cancel()
    }

    fun startGetServiceLength() {
        jobForServiceLength = viewModelScope.launch(Dispatchers.IO) {
            getServiceLength()
        }
        Log.d(LOG_SERVICE_LENGTH, "START get service length")
        Log.d(LOG_NEXT_CUSTOMER_INFO, "START get next customer")
    }

    fun stopGetServiceLength() {
        jobForServiceLength?.cancel()
        Log.d(LOG_SERVICE_LENGTH, "STOP get service length")
        Log.d(LOG_NEXT_CUSTOMER_INFO, "STOP get next customer")
    }
}
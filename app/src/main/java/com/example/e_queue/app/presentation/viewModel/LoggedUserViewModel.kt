package com.example.e_queue.app.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_queue.app.data.model.BodyForFinishWorkWithCustomer
import com.example.e_queue.app.data.model.InviteNextCustomerInfo
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.app.data.model.NextCustomerInfo
import com.example.e_queue.app.domain.repository.EQueueRepository
import com.example.e_queue.utils.Constants.Companion.LOG_NEXT_CUSTOMER_INFO
import com.example.e_queue.utils.Constants.Companion.LOG_SERVICE_LENGTH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoggedUserViewModel constructor(
    private val eQueueRepository: EQueueRepository,
    val loggedUserModel: LoggedUser
) : ViewModel() {

    private var jobForServiceLength: Job? = null
    private var jobForNextCustomerInfo: Job? = null

    private val _loggedUser = MutableLiveData<LoggedUser>()
    val loggedUser: LiveData<LoggedUser> = _loggedUser

    private val _serviceLength = MutableLiveData<Int>()
    val serviceLength: LiveData<Int> = _serviceLength

    private val _nextCustomerInfo = MutableLiveData<NextCustomerInfo>()
    val nextCustomerInfo: LiveData<NextCustomerInfo> = _nextCustomerInfo

    private val _inviteNextCustomerInfo = MutableLiveData<InviteNextCustomerInfo>()
    val inviteNextCustomerInfo: LiveData<InviteNextCustomerInfo> = _inviteNextCustomerInfo

    fun setUserParams() {
        _loggedUser.postValue(loggedUserModel)
    }

    private suspend fun getServiceLength() {
        while (true) {
            val length = eQueueRepository.getSelfServices(loggedUserModel.id)
            if (length.selfServices.isNotEmpty()) {
                var sum = 0
                length.selfServices.forEach { value ->
                    sum += value.line.count()
                }
                Log.d(
                    LOG_SERVICE_LENGTH,
                    "Now length in service - $sum for user: ${loggedUserModel.name}"
                )
                _serviceLength.postValue(sum)
            }
            delay(5000)
        }
    }

    fun inviteNextCustomer() {
        runCatching {
            viewModelScope.launch(Dispatchers.IO) {
                val nextCustomerInfo = eQueueRepository.inviteNextCustomer(loggedUserModel.id)
                _inviteNextCustomerInfo.postValue(nextCustomerInfo)
            }
        }
    }

    fun killNextCustomer() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                eQueueRepository.killNextCustomer(loggedUserModel.id)
            }
        }
    }

    fun getStartCustomer() {
        viewModelScope.launch(Dispatchers.IO) {
            eQueueRepository.getStartCustomer(loggedUserModel.id)
        }
    }

    private suspend fun getNextCustomerInfo() {
        while (true) {
            try {
                val customer = eQueueRepository.getNextCustomerInfo(loggedUserModel.id)
                _nextCustomerInfo.postValue(customer)
                Log.d(
                    LOG_NEXT_CUSTOMER_INFO,
                    "Next customer - ${customer.prefix}${customer.number} "
                )
            } catch (error: HttpException) {
                val customer = NextCustomerInfo(
                    number = "",
                    prefix = ""
                )
                if (error.code() == 404) {
                    _nextCustomerInfo.postValue(customer)
                    Log.d(LOG_NEXT_CUSTOMER_INFO, "No next Customer!")
                }
            }
            delay(5000)
        }
    }

    fun finishWorkWithCustomer(body: BodyForFinishWorkWithCustomer) {
        viewModelScope.launch(Dispatchers.IO) {
            eQueueRepository.finishWorkWithCustomer(body)
        }
    }

    fun startGetNextCustomerInfo() {
        jobForNextCustomerInfo = viewModelScope.launch(Dispatchers.IO) {
            getNextCustomerInfo()
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
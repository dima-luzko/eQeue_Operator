package com.example.e_queue.app.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_queue.MainApplication
import com.example.e_queue.app.data.model.BodyForFinishWorkWithCustomer
import com.example.e_queue.app.data.model.InviteNextCustomerInfo
import com.example.e_queue.app.data.model.LoggedUser
import com.example.e_queue.app.data.model.NextCustomerInfo
import com.example.e_queue.app.domain.repository.EQueueRepository
import com.example.e_queue.utils.Constants.Companion.LOG_NEXT_CUSTOMER_INFO
import com.example.e_queue.utils.Constants.Companion.LOG_SERVICE_LENGTH
import com.example.e_queue.utils.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class LoggedUserViewModel constructor(
    private val eQueueRepository: EQueueRepository,
    val loggedUserModel: LoggedUser
) : ViewModel() {

    private var previousLengthTotal = 0

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

    private val _isPlay = MutableLiveData<Boolean>()
    val isPlay: LiveData<Boolean> = _isPlay

    fun setUserParams() {
        _loggedUser.postValue(loggedUserModel)
    }

    private suspend fun getServiceLength() {
        while (true) {
            try {
                val length = eQueueRepository.getSelfServices(
                    url = "http://${
                        PreferencesManager.getInstance(MainApplication().getAppContext())
                            .getString(PreferencesManager.PREF_GLUE_IP, "127.0.0.1:8080")
                    }/api/operator/getSelfServices",
                    userId = loggedUserModel.id
                )
                if (length.selfServices.isNotEmpty()) {
                    var count = 0
                    length.selfServices.forEach { value ->
                        count += value.line.count()
                    }
                    Log.d(
                        LOG_SERVICE_LENGTH,
                        "Now length in service - $count for user: ${loggedUserModel.name}"
                    )
                    _serviceLength.postValue(count)
                    if (previousLengthTotal == 0 && count > 0) {
                        previousLengthTotal = count
                        _isPlay.postValue(true)
                    } else if (count == 0) {
                        previousLengthTotal = count
                    }
                }
            } catch (exception: SocketTimeoutException) {
            }
            delay(5000)
        }
    }

    fun inviteNextCustomer() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val nextCustomerInfo = eQueueRepository.inviteNextCustomer(
                    url = "http://${
                        PreferencesManager.getInstance(MainApplication().getAppContext())
                            .getString(PreferencesManager.PREF_GLUE_IP, "127.0.0.1:8080")
                    }/api/operator/inviteNextCustomer",
                    userId = loggedUserModel.id
                )
                _inviteNextCustomerInfo.postValue(nextCustomerInfo)
            }.onFailure { }
        }
    }

    fun killNextCustomer() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                eQueueRepository.killNextCustomer(
                    url = "http://${
                        PreferencesManager.getInstance(MainApplication().getAppContext())
                            .getString(PreferencesManager.PREF_GLUE_IP, "127.0.0.1:8080")
                    }/api/operator/killNextCustomer",
                    userId = loggedUserModel.id
                )
            }
        }
    }

    fun getStartCustomer() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                eQueueRepository.getStartCustomer(
                    url = "http://${
                        PreferencesManager.getInstance(MainApplication().getAppContext())
                            .getString(PreferencesManager.PREF_GLUE_IP, "127.0.0.1:8080")
                    }/api/operator/getStartCustomer",
                    userId = loggedUserModel.id
                )
            }.onFailure {  }
        }
    }

    private suspend fun getNextCustomerInfo() {
        while (true) {
            try {
                val customer = eQueueRepository.getNextCustomerInfo(
                    url = "http://${
                        PreferencesManager.getInstance(MainApplication().getAppContext())
                            .getString(PreferencesManager.PREF_GLUE_IP, "127.0.0.1:8080")
                    }/api/operator/getNextCustomerInfo",
                    userId = loggedUserModel.id
                )
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
            } catch (exception: SocketTimeoutException) {
            }
            delay(5000)
        }
    }

    fun finishWorkWithCustomer(body: BodyForFinishWorkWithCustomer) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                eQueueRepository.finishWorkWithCustomer(
                    url = "http://${
                        PreferencesManager.getInstance(MainApplication().getAppContext())
                            .getString(PreferencesManager.PREF_GLUE_IP, "127.0.0.1:8080")
                    }/api/operator/getFinishCustomer",
                    result = body
                )
            }.onFailure {  }
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
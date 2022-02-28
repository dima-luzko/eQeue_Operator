package com.example.e_queue.app.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_queue.MainApplication
import com.example.e_queue.app.data.model.BodyForFinishWorkWithCustomer
import com.example.e_queue.app.data.model.BodyForPostponedCustomer
import com.example.e_queue.app.data.model.BodyForRedirectCustomer
import com.example.e_queue.app.data.model.OperationWithLoggedUser
import com.example.e_queue.app.domain.repository.EQueueRepository
import com.example.e_queue.utils.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OperationWithLoggedUserViewModel constructor(
    private val eQueueRepository: EQueueRepository,
    val operationWithUser: OperationWithLoggedUser
) :
    ViewModel() {

    private val _operationWithLoggedUser = MutableLiveData<OperationWithLoggedUser>()
    val operationWithLoggedUser: LiveData<OperationWithLoggedUser> = _operationWithLoggedUser

    fun setParams() {
        _operationWithLoggedUser.postValue(operationWithUser)
    }

    fun redirectCustomer(body: BodyForRedirectCustomer) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                eQueueRepository.redirectCustomer(
                    url = "http://${
                        PreferencesManager.getInstance(MainApplication().getAppContext())
                            .getString(PreferencesManager.PREF_GLUE_IP, "127.0.0.1:8080")
                    }/api/operator/redirectCustomer",
                    customer = body
                )
            }.onFailure {  }
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

    fun customerToPostpone(body: BodyForPostponedCustomer) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                eQueueRepository.customerToPostpone(
                    url = "http://${
                        PreferencesManager.getInstance(MainApplication().getAppContext())
                            .getString(PreferencesManager.PREF_GLUE_IP, "127.0.0.1:8080")
                    }/api/operator/customerToPostpone",
                    customer = body
                )
            }.onFailure {  }
        }
    }
}
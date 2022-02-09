package com.example.e_queue.app.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_queue.app.data.model.BodyForFinishWorkWithCustomer
import com.example.e_queue.app.data.model.BodyForPostponedCustomer
import com.example.e_queue.app.data.model.BodyForRedirectCustomer
import com.example.e_queue.app.data.model.OperationWithLoggedUser
import com.example.e_queue.app.domain.repository.EQueueRepository
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
            eQueueRepository.redirectCustomer(body)
        }
    }

    fun finishWorkWithCustomer(body: BodyForFinishWorkWithCustomer) {
        viewModelScope.launch(Dispatchers.IO) {
            eQueueRepository.finishWorkWithCustomer(body)
        }
    }

    fun customerToPostpone(body: BodyForPostponedCustomer){
        viewModelScope.launch(Dispatchers.IO) {
            eQueueRepository.customerToPostpone(body)
        }
    }
}
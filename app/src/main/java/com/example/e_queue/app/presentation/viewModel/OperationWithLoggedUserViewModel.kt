package com.example.e_queue.app.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun killNextCustomer() {
        viewModelScope.launch(Dispatchers.IO){
            operationWithLoggedUser.value?.let { eQueueRepository.killNextCustomer(it.userId) }
        }
    }
}
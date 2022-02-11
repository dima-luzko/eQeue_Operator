package com.example.e_queue.app.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_queue.app.data.model.InviteNextCustomerInfo
import com.example.e_queue.app.domain.repository.EQueueRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostponedListViewModel constructor(private val eQueueRepository: EQueueRepository) :
    ViewModel() {

    private val _results = MutableLiveData<List<InviteNextCustomerInfo>>()
    val results: LiveData<List<InviteNextCustomerInfo>> = _results

    private val _selectedClientId = MutableLiveData<Long>()
    val selectedClientId: LiveData<Long> = _selectedClientId

    private val _postponedClientLength = MutableLiveData<String>()
    val postponedClientLength: LiveData<String> = _postponedClientLength

    private val _invitePostponedCustomer = MutableLiveData<InviteNextCustomerInfo>()
    val invitePostponedCustomer: LiveData<InviteNextCustomerInfo> = _invitePostponedCustomer

    fun getPostponedClientList() {
        viewModelScope.launch(Dispatchers.IO) {
            val postponedClientList = eQueueRepository.getPostponedPoolInfo()
            _results.postValue(postponedClientList)
        }
    }

    fun getSelectedResultsId(resultId: Long) {
        _selectedClientId.postValue(resultId)
    }

    fun setPostponedLength() {
        viewModelScope.launch(Dispatchers.IO) {
            val length = eQueueRepository.getPostponedPoolInfo()
            _postponedClientLength.postValue(length.size.toString())
        }
    }

    fun invitePostponedCustomer(userId: Int, customerId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            val res = eQueueRepository.invitePostponedCustomer(userId, customerId)
            _invitePostponedCustomer.postValue(res)
        }
    }

}

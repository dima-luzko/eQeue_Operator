package com.example.e_queue.app.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_queue.app.data.model.ServicesList
import com.example.e_queue.app.domain.repository.EQueueRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ServicesListViewModel constructor(private val eQueueRepository: EQueueRepository) : ViewModel() {
    private val _services = MutableLiveData<ServicesList>()
    val services: LiveData<ServicesList> = _services



    fun getServicesList() {
        viewModelScope.launch(Dispatchers.IO){
            val userList = eQueueRepository.getServices()
            _services.postValue(userList)
        }
    }

}
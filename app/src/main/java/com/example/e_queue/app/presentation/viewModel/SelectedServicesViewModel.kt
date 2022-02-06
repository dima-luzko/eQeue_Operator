package com.example.e_queue.app.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_queue.app.data.model.SelectedServices

class SelectedServicesViewModel : ViewModel() {

    private val _services = MutableLiveData<SelectedServices>()
    val services: LiveData<SelectedServices> = _services

    fun setServices(service: SelectedServices){
        _services.postValue(service)
    }
}
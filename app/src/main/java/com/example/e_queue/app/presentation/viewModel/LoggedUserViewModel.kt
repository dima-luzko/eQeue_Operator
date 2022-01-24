package com.example.e_queue.app.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_queue.app.data.model.LoggedUser

class LoggedUserViewModel constructor(id: Int, point: String, name: String, serviceId: Long?) :
    ViewModel() {

    private val _loggedUser = MutableLiveData<LoggedUser>()
    val loggedUser: LiveData<LoggedUser> = _loggedUser

    var user = LoggedUser(id = id, point = point, name = name, service_id = serviceId)

    fun setUserParams() {
        _loggedUser.postValue(user)
    }
}
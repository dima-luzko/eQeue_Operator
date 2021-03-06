package com.example.e_queue.app.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_queue.MainApplication
import com.example.e_queue.app.data.model.User
import com.example.e_queue.app.domain.repository.EQueueRepository
import com.example.e_queue.utils.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserListViewModel constructor(private val eQueueRepository: EQueueRepository) : ViewModel() {
    private val _user = MutableLiveData<List<User>>()
    val user: LiveData<List<User>> = _user

    private var userList: List<User> = listOf()

    fun getUserList() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                userList = eQueueRepository.getUsers("http://${
                    PreferencesManager.getInstance(MainApplication().getAppContext())
                        .getString(PreferencesManager.PREF_GLUE_IP, "127.0.0.1:8080")
                }/api/operator/getUsers")
            }.onSuccess {
                _user.postValue(userList)
            }.onFailure {}
        }
    }

}
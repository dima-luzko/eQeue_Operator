package com.example.e_queue.app.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_queue.app.data.model.SelectedUser

class SelectedUserViewModel : ViewModel(){
    private val _userName = MutableLiveData<SelectedUser>()
    val userName: LiveData<SelectedUser> = _userName

    fun setUserName(user: SelectedUser){
        _userName.postValue(user)
    }
}
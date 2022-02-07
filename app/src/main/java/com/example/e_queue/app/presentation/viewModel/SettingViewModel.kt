package com.example.e_queue.app.presentation.viewModel

import android.content.Context
import androidx.lifecycle.*
import com.example.e_queue.utils.PreferencesManager

class SettingViewModel() : ViewModel() {
    private val _switchPostponed = MutableLiveData<Boolean>()
    val switchPostponed: LiveData<Boolean> = _switchPostponed

    private val _switchRedirect = MutableLiveData<Boolean>()
    val switchRedirect: LiveData<Boolean> = _switchRedirect

    private val _switchOneButtonMode = MutableLiveData<Boolean>()
    val switchOneButtonMode: LiveData<Boolean> = _switchOneButtonMode

    fun changeStateSwitchPostponed(switchState: Boolean) {
        _switchPostponed.postValue(switchState)
    }

    fun changeStateSwitchRedirect(switchState: Boolean) {
        _switchRedirect.postValue(switchState)
    }

    fun changeStateSwitchOneButtonMode(switchState: Boolean) {
        _switchOneButtonMode.postValue(switchState)
    }


    class UiState(
        val first: Boolean,
        val second: Boolean,
        val third: Boolean
    )

    val uiState: LiveData<UiState> = MediatorLiveData<UiState>().apply {
        val observer = Observer<Boolean> {
            val postponed = _switchPostponed.value
            val redirect = _switchRedirect.value
            val oneMode = _switchOneButtonMode.value

            value = UiState(
                first = oneMode == true,
                second = oneMode == true,
                third = postponed == true || redirect == true
            )
        }

        addSource(_switchPostponed, observer)
        addSource(_switchRedirect, observer)
        addSource(_switchOneButtonMode, observer)
    }
}
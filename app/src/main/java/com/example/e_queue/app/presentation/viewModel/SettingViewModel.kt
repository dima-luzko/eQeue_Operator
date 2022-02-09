package com.example.e_queue.app.presentation.viewModel

import android.content.Context
import androidx.lifecycle.*
import com.example.e_queue.utils.PreferencesManager

class SettingViewModel(context: Context) : ViewModel() {
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
            val postponed = PreferencesManager.getInstance(context)
                .getBoolean(PreferencesManager.PREF_SWITCH_POSTPONED, false)
            val redirect = PreferencesManager.getInstance(context)
                .getBoolean(PreferencesManager.PREF_SWITCH_REDIRECT, false)
            val oneMode = PreferencesManager.getInstance(context)
                .getBoolean(PreferencesManager.PREF_SWITCH_ONE_MODE, false)

            value = UiState(
                first = oneMode,
                second = oneMode,
                third = postponed || redirect
            )
        }

        addSource(_switchPostponed, observer)
        addSource(_switchRedirect, observer)
        addSource(_switchOneButtonMode, observer)
    }
}
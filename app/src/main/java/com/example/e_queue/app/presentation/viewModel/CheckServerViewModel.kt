package com.example.e_queue.app.presentation.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_queue.MainApplication
import com.example.e_queue.app.domain.repository.EQueueRepository
import com.example.e_queue.utils.Constants
import com.example.e_queue.utils.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CheckServerViewModel constructor(private val eQueueRepository: EQueueRepository) :
    ViewModel() {

    private val _statusWorkServer = MutableLiveData<Boolean>()
    val statusWorkServer: LiveData<Boolean> = _statusWorkServer

    private var job: Job? = null

    private suspend fun checkServerState() {
        while (true) {
            runCatching {
                eQueueRepository.checkHealth()
            }.onSuccess {
                _statusWorkServer.postValue(true)
                Log.d(
                    Constants.LOG_SERVER_STATE, "server work! ip: ${
                        PreferencesManager.getInstance(MainApplication().getAppContext())
                            .getString(PreferencesManager.PREF_GLUE_IP, "127.0.0.1:8080")
                    }"
                )
            }.onFailure {
                _statusWorkServer.postValue(false)
                Log.d(
                    Constants.LOG_SERVER_STATE, "server not working! ip: ${
                        PreferencesManager.getInstance(MainApplication().getAppContext())
                            .getString(PreferencesManager.PREF_GLUE_IP, "127.0.0.1:8080")
                    }"
                )
            }
            delay(5000)
        }
    }

    fun startCheckServerState() {
        job = viewModelScope.launch(Dispatchers.IO) {
            checkServerState()
        }
        Log.d(Constants.LOG_SERVER_STATE, "START check server state")
    }

    fun stopCheckServerState() {
        job?.cancel()
        Log.d(Constants.LOG_SERVER_STATE, "STOP check server state")
    }
}
package com.example.e_queue.app.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_queue.MainApplication
import com.example.e_queue.app.data.model.ResultList
import com.example.e_queue.app.domain.repository.EQueueRepository
import com.example.e_queue.utils.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultsListViewModel constructor(private val eQueueRepository: EQueueRepository) :
    ViewModel() {

    private val _results = MutableLiveData<ResultList>()
    val results: LiveData<ResultList> = _results

    private val _selectedResultsId = MutableLiveData<Int>()
    val selectedResults: LiveData<Int> = _selectedResultsId

    fun getResultsList() {
        viewModelScope.launch(Dispatchers.IO) {
            val resultsList = eQueueRepository.getResultsList(
                url = "http://${
                    PreferencesManager.getInstance(MainApplication().getAppContext())
                        .getString(PreferencesManager.PREF_GLUE_IP, "127.0.0.1:8080")
                }/api/operator/getResultsList"
            )
            _results.postValue(resultsList)
        }
    }

    fun getSelectedResultsId(resultId: Int) {
       _selectedResultsId.postValue(resultId)
    }

}
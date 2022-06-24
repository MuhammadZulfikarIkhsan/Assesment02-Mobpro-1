package org.d3if2049.tolist.ui.negara

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if2049.tolist.model.Negara
import org.d3if2049.tolist.network.ApiStatus
import org.d3if2049.tolist.network.NegaraApi

class NegaraViewModel : ViewModel() {

    private val data = MutableLiveData<List<Negara>>()
    private val status = MutableLiveData<ApiStatus>()

    init {
        retrieveData()
    }

    private fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.postValue(ApiStatus.LOADING)
            try {
                data.postValue(NegaraApi.service.getNegara())
                status.postValue(ApiStatus.SUCCESS)
            } catch (e: Exception) {
                Log.d("NegaraViewModel", "Failure: ${e.message}")
                status.postValue(ApiStatus.FAILED)
            }
        }
    }

    fun getData(): LiveData<List<Negara>> = data

    fun getStatus(): LiveData<ApiStatus> = status

}
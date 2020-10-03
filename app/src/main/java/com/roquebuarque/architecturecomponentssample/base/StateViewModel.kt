package com.roquebuarque.architecturecomponentssample.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class StateViewModel<State> : ViewModel() {

    val state: LiveData<State>
        get() {
            start()
            return mutableState
        }


    protected abstract val mutableState: MutableLiveData<State>

    protected abstract fun stateFlow(): Flow<State>

    private fun start() {
        viewModelScope.launch {
            stateFlow().collect {
                mutableState.postValue(it)
            }
        }
    }
}

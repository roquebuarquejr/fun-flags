package com.roquebuarque.architecturecomponentssample.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.roquebuarque.architecturecomponentssample.base.BaseState.Status.ERROR
import com.roquebuarque.architecturecomponentssample.base.BaseState.Status.SUCCESS
import kotlinx.coroutines.Dispatchers

fun <T, A> performNetworkRequest(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> BaseState<A>,
    saveCallResult: suspend (A) -> Unit
): LiveData<BaseState<T>> =
    liveData(Dispatchers.IO) {
        emit(BaseState.loading())
        val source = databaseQuery().map { BaseState.success(it) }
        emitSource(source)

        val responseStatus = networkCall()
        if (responseStatus.status == SUCCESS) {
            responseStatus.data?.let {
                saveCallResult(it)
            }

        } else if (responseStatus.status == ERROR) {
            responseStatus.message?.let {
                emit(BaseState.error(message = it, data = null))
            }
            emitSource(source)
        }
    }
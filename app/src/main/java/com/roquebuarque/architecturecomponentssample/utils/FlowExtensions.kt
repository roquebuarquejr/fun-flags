package com.roquebuarque.architecturecomponentssample.utils

import android.view.View
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.flow.Flow

fun TextView.doAfterTextChangedFlow(): Flow<String> {
    return MutableLiveData<String>().also { data ->
        doAfterTextChanged {
            data.postValue(it.toString())
        }
    }.asFlow()

}


fun View.setOnClickListenerFlow(): Flow<Unit> {
    return MutableLiveData<Unit>().also {
        this.setOnClickListener { _ ->
            it.postValue(Unit)
        }
    }.asFlow()
}

fun SwipeRefreshLayout.setOnRefreshListenerFlow(): Flow<Unit> {
    return MutableLiveData<Unit>().also {
        this.setOnRefreshListener {
            it.postValue(Unit)
        }
    }.asFlow()
}
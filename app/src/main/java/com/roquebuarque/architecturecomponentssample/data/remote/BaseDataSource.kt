package com.roquebuarque.architecturecomponentssample.data.remote

import com.roquebuarque.architecturecomponentssample.base.BaseState
import retrofit2.Response
import timber.log.Timber

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): BaseState<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return BaseState.success(body)
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): BaseState<T> {
        Timber.d(message)
        return BaseState.error("Network call has failed: $message")
    }

}
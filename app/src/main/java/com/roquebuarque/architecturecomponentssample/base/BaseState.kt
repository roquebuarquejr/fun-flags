package com.roquebuarque.architecturecomponentssample.base

data class BaseState<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): BaseState<T> {
            return BaseState(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): BaseState<T> {
            return BaseState(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): BaseState<T> {
            return BaseState(Status.LOADING, data, null)
        }
    }
}
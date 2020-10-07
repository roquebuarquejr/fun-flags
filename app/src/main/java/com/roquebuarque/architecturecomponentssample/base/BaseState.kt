package com.roquebuarque.architecturecomponentssample.base

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class BaseState<out T>(val status: Status, val data: @RawValue T?, val message: String?): Parcelable {

    enum class Status {
        SUCCESS,
        ERROR
    }

    companion object {
        fun <T> success(data: T): BaseState<T> {
            return BaseState(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): BaseState<T> {
            return BaseState(Status.ERROR, data, message)
        }
    }
}
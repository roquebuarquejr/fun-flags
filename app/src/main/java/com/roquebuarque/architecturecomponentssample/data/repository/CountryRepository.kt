package com.roquebuarque.architecturecomponentssample.data.repository

import androidx.lifecycle.map
import com.roquebuarque.architecturecomponentssample.base.BaseState
import com.roquebuarque.architecturecomponentssample.base.performNetworkRequest
import com.roquebuarque.architecturecomponentssample.data.local.CountryDao
import com.roquebuarque.architecturecomponentssample.data.remote.CountryRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryRepository @Inject constructor(
    private val remoteDataSource: CountryRemoteDataSource,
    private val localDataSource: CountryDao
) {

    fun getAllCountries() = performNetworkRequest(
        databaseQuery = { localDataSource.getAllCountries() },
        networkCall = { remoteDataSource.getAllCountries() },
        saveCallResult = { localDataSource.insertAll(it) }
    )

    fun getCountryByName(name: String) =
        localDataSource
            .getCountries(name)
            .map {
                BaseState.success(data = it)
            }
}
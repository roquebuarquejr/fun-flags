package com.roquebuarque.architecturecomponentssample.feature.country.data.remote

import com.roquebuarque.architecturecomponentssample.base.BaseDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryRemoteDataSource @Inject constructor(
    private val countryService: CountryService
): BaseDataSource() {

    suspend fun getAllCountries() = getResult { countryService.getAllCountries() }
}
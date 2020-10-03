package com.roquebuarque.architecturecomponentssample.data.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryRemoteDataSource @Inject constructor(
    private val countryService: CountryService
): BaseDataSource() {

    suspend fun getAllCountries() = getResult { countryService.getAllCountries() }
}
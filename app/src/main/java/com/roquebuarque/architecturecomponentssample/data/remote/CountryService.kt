package com.roquebuarque.architecturecomponentssample.data.remote

import com.roquebuarque.architecturecomponentssample.data.entities.CountryDto
import retrofit2.Response
import retrofit2.http.GET

interface CountryService {

    @GET("v2/all")
    suspend fun getAllCountries() : Response<List<CountryDto>>

}
package com.roquebuarque.architecturecomponentssample.feature.country.domain

import com.roquebuarque.architecturecomponentssample.base.BaseState
import com.roquebuarque.architecturecomponentssample.feature.country.data.entities.CountryDto
import com.roquebuarque.architecturecomponentssample.feature.country.presentation.CountryListState
import javax.inject.Inject


/**
 * Convert [BaseState] to [CountryListState]
 */

class CountryListMapper @Inject constructor() {

    operator fun invoke(baseState: BaseState<List<CountryDto>>): CountryListState =
        when (baseState.status) {
            BaseState.Status.SUCCESS -> {
                if (baseState.data != null && baseState.data.isNotEmpty()) {
                    CountryListState.Success(baseState.data)
                } else {
                    CountryListState.Empty
                }
            }
            BaseState.Status.ERROR -> CountryListState.Message(baseState.message ?: "Something went wrong")
        }

}
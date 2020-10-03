package com.roquebuarque.architecturecomponentssample.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import com.roquebuarque.architecturecomponentssample.base.BaseState
import com.roquebuarque.architecturecomponentssample.base.StateViewModel
import com.roquebuarque.architecturecomponentssample.data.entities.CountryDto
import com.roquebuarque.architecturecomponentssample.data.repository.CountryRepository
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow


@FlowPreview
@ExperimentalCoroutinesApi
@ActivityScoped
class CountryListViewModel @ViewModelInject constructor(
    private val repository: CountryRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : StateViewModel<BaseState<List<CountryDto>>>() {

    override val mutableState =
        savedStateHandle.getLiveData<BaseState<List<CountryDto>>>("CountryListViewModel.state")


    private fun fetchAllUsers(): Flow<BaseState<List<CountryDto>>> {
        return repository.getAllCountries().asFlow()
    }

    override fun stateFlow(): Flow<BaseState<List<CountryDto>>> {
        return fetchAllUsers()
    }
}


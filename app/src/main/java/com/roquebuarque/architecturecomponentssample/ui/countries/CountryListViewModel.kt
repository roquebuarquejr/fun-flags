package com.roquebuarque.architecturecomponentssample.ui.countries

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.roquebuarque.architecturecomponentssample.base.BaseState
import com.roquebuarque.architecturecomponentssample.base.StateViewModel
import com.roquebuarque.architecturecomponentssample.data.entities.CountryDto
import com.roquebuarque.architecturecomponentssample.data.repository.CountryRepository
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


@FlowPreview
@ExperimentalCoroutinesApi
@ActivityScoped
class CountryListViewModel @ViewModelInject constructor(
    private val repository: CountryRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : StateViewModel<BaseState<List<CountryDto>>>() {

    override val mutableState =
        savedStateHandle.getLiveData<BaseState<List<CountryDto>>>("CountryListViewModel.state")

    private val shouldRefreshAllUsers = BroadcastChannel<Unit>(1)

    fun refresh() {
        viewModelScope.launch {
            shouldRefreshAllUsers.send(Unit)
        }
    }

    private fun refreshAllUsers(): Flow<BaseState<List<CountryDto>>> {
        return shouldRefreshAllUsers
            .asFlow()
            .flatMapLatest { fetchAllCountries() }
    }

    private fun fetchAllCountries(): Flow<BaseState<List<CountryDto>>> {
        return repository.getAllCountries().asFlow()
    }

    override fun stateFlow(): Flow<BaseState<List<CountryDto>>> {
        return merge(refreshAllUsers(), fetchAllCountries())
    }
}


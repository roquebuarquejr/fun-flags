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
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


@FlowPreview
@ExperimentalCoroutinesApi
@FragmentScoped
class CountryListViewModel @ViewModelInject constructor(
    private val repository: CountryRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : StateViewModel<BaseState<List<CountryDto>>>() {

    companion object {
        private const val STATE_KEY = "CountryListViewModel.state"
    }

    override val mutableState =
        savedStateHandle.getLiveData<BaseState<List<CountryDto>>>(STATE_KEY)

    private val actionBroadcastChannel = BroadcastChannel<CountryListIntent>(1)

    /**
     * Trigger user intent actions
     */
    fun intent(intent: CountryListIntent) {
        viewModelScope.launch {
            actionBroadcastChannel.send(intent)
        }
    }

    private val actions = actionBroadcastChannel
        .asFlow()
        .flatMapLatest {
            when (it) {
                is CountryListIntent.Refresh -> fetchAllCountries()
                is CountryListIntent.Search -> searchCountry(it.query)
            }

        }

    private fun searchCountry(name: String): Flow<BaseState<List<CountryDto>>> {
        return repository
            .getCountryByName(name)
            .asFlow()
            .onEach {
                savedStateHandle.set(STATE_KEY, it)
            }
    }


    private fun fetchAllCountries(): Flow<BaseState<List<CountryDto>>> {
        return repository
            .getAllCountries()
            .asFlow()
            .onEach {
                savedStateHandle.set(STATE_KEY, it)
            }
    }

    override fun stateFlow(): Flow<BaseState<List<CountryDto>>> {
        return merge(actions, fetchAllCountries())
    }
}

/**
 * Handle all user interactions for [CountryListFragment]
 */
sealed class CountryListIntent {

    /**
     * When search for specific country
     */
    data class Search(val query: String) : CountryListIntent()

    /**
     * When swipe refresh trigger
     */
    object Refresh : CountryListIntent()
}


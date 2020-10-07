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
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
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

    private val actionBroadcastChannel = ConflatedBroadcastChannel<CountryListIntent>()

    /**
     * Trigger user intent actions
     */
    fun intent(intent: CountryListIntent) {
        viewModelScope.launch {
            actionBroadcastChannel.send(intent)
        }
    }

    fun intent(intent: Flow<CountryListIntent>) {
        viewModelScope.launch {
            intent.collect {
                actionBroadcastChannel.send(it)
            }
        }
    }

    private val actions = actionBroadcastChannel
        .asFlow()
        .flatMapLatest {
            when (it) {
                is CountryListIntent.Refresh -> fetchAllCountries()
                is CountryListIntent.Search -> searchCountry(it.query)
                CountryListIntent.CleanSearch -> clearSearch()
            }
        }

    private fun clearSearch(): Flow<BaseState<List<CountryDto>>> = fetchAllCountries()


    private fun searchCountry(name: String): Flow<BaseState<List<CountryDto>>> {
        return repository
            .getCountryByName(name)
            .asFlow()
    }


    private fun fetchAllCountries(): Flow<BaseState<List<CountryDto>>> {
        return repository
            .getAllCountries()
            .asFlow()
    }

    override fun stateFlow(): Flow<BaseState<List<CountryDto>>> {
        return merge(actions, fetchAllCountries())
            .onEach {
                savedStateHandle.set(STATE_KEY, it)
            }
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

    /**
     * When edit text string is empty
     */
    object CleanSearch : CountryListIntent()


}


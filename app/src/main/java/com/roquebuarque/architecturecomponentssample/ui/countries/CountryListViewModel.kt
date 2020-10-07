package com.roquebuarque.architecturecomponentssample.ui.countries

import android.os.Parcelable
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.roquebuarque.architecturecomponentssample.base.BaseState
import com.roquebuarque.architecturecomponentssample.base.BaseState.Status.ERROR
import com.roquebuarque.architecturecomponentssample.base.BaseState.Status.SUCCESS
import com.roquebuarque.architecturecomponentssample.base.StateViewModel
import com.roquebuarque.architecturecomponentssample.data.entities.CountryDto
import com.roquebuarque.architecturecomponentssample.data.repository.CountryRepository
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.android.parcel.Parcelize
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
) : StateViewModel<CountryListState>() {

    companion object {
        private const val STATE_KEY = "CountryListViewModel.state"
    }

    override val mutableState =
        savedStateHandle.getLiveData<CountryListState>(STATE_KEY)

    private val actionBroadcastChannel = ConflatedBroadcastChannel<CountryListIntent>()

    /**
     * Trigger user intent actions
     */
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
                CountryListIntent.CleanSearch -> fetchAllCountries(true)
            }
        }

    private fun searchCountry(name: String): Flow<CountryListState> {
        return liveData {
            repository
                .getCountryByName(name)
                .asFlow()
                .onStart { emit(CountryListState.SearchMode) }
                .collect { emit(CountryListMapper(it)) }

        }.asFlow()
    }


    private fun fetchAllCountries(shouldClearSearch: Boolean = false): Flow<CountryListState> {
        return liveData {
            repository
                .getAllCountries()
                .asFlow()
                .onStart { emit(CountryListState.Loading(true)) }
                .collect {
                    emit(CountryListMapper(it))
                    emit(CountryListState.Loading(false))

                    if (shouldClearSearch)
                        emit(CountryListState.ClearSearch)
                }

        }.asFlow()
    }

    override fun stateFlow(): Flow<CountryListState> {
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

/**
 * Convert [BaseState] to [CountryListState]
 */
object CountryListMapper {

    operator fun invoke(baseState: BaseState<List<CountryDto>>): CountryListState =
        when (baseState.status) {
            SUCCESS -> {
                if (baseState.data != null && baseState.data.isNotEmpty()) {
                    CountryListState.Success(baseState.data)
                } else {
                    CountryListState.Empty
                }
            }
            ERROR -> CountryListState.Message(baseState.message ?: "Something went wrong")
        }

}

/**
 * State of view [CountryListFragment]
 */

sealed class CountryListState {

    /**
     * Handle loading state to show/hide progressbar
     */
    @Parcelize
    data class Loading(val isLoading: Boolean) : CountryListState(), Parcelable

    /**
     * Handle empty state to show when country list is empty
     */
    @Parcelize
    object Empty : CountryListState(), Parcelable

    /**
     * Handle all type of message that should be display
     */
    @Parcelize
    data class Message(val text: String) : CountryListState(), Parcelable

    /**
     * Trigger when the list is success fetched from repository
     */
    @Parcelize
    data class Success(val data: List<CountryDto>) : CountryListState(), Parcelable

    /**
     * Clear the search result and hide the keyboard
     */
    @Parcelize
    object ClearSearch : CountryListState(), Parcelable

    /**
     * Clear the search result and hide the keyboard
     */
    @Parcelize
    object SearchMode : CountryListState(), Parcelable


}


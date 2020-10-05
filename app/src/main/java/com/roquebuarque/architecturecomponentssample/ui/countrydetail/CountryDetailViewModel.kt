package com.roquebuarque.architecturecomponentssample.ui.countrydetail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.roquebuarque.architecturecomponentssample.base.BaseState
import com.roquebuarque.architecturecomponentssample.base.StateViewModel
import com.roquebuarque.architecturecomponentssample.data.entities.CountryDto
import com.roquebuarque.architecturecomponentssample.data.local.CountryDao
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
@FragmentScoped
class CountryDetailViewModel @ViewModelInject constructor(
    private val local: CountryDao,
    @Assisted private val savedStateHandle: SavedStateHandle
) : StateViewModel<CountryDto>() {

    companion object {
        private const val STATE_KEY = "CountryDetailViewModel.state"
    }

    override val mutableState =
        savedStateHandle.getLiveData<CountryDto>(STATE_KEY)

    private val nameBroadCastChannel = BroadcastChannel<String>(1)

    fun start(name: String) {
        viewModelScope.launch {
            nameBroadCastChannel.send(name)
        }
    }

    private fun fetchCountryFromLocal(): Flow<CountryDto> {
        return nameBroadCastChannel
            .asFlow()
            .flatMapLatest {
                local
                    .getCountry(it)
                    .asFlow()
                    .onEach { data ->
                        savedStateHandle.set(STATE_KEY, data)
                    }
            }
    }

    override fun stateFlow(): Flow<CountryDto> {
        return fetchCountryFromLocal()
    }

}
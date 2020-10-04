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
    @Assisted savedStateHandle: SavedStateHandle
) : StateViewModel<CountryDto>() {

    override val mutableState =
        savedStateHandle.getLiveData<CountryDto>("CountryDetailViewModel.state")

    private val idBroadCastChannel = BroadcastChannel<Int>(1)

    fun start(id: Int) {
        viewModelScope.launch {
            idBroadCastChannel.send(id)
        }
    }

    private fun fetchCountryFromLocal(): Flow<CountryDto> {
        return idBroadCastChannel
            .asFlow()
            .flatMapLatest {
                local
                    .getCountry(it)
                    .asFlow()
            }
    }

    override fun stateFlow(): Flow<CountryDto> {
        return fetchCountryFromLocal()
    }

}
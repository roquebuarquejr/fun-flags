package com.roquebuarque.architecturecomponentssample.ui.countries

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.roquebuarque.architecturecomponentssample.R
import com.roquebuarque.architecturecomponentssample.base.BaseState
import com.roquebuarque.architecturecomponentssample.data.entities.CountryDto
import com.roquebuarque.architecturecomponentssample.ui.countrydetail.CountryDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_country_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class CountryListFragment : Fragment(R.layout.fragment_country_list) {

    private val viewModel: CountryListViewModel by viewModels()
    private val adapter: CountryListAdapter by lazy { CountryListAdapter(::countryListClicked) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupObserver()

        countryListSwipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupObserver() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state.status) {
                BaseState.Status.SUCCESS -> {
                    load(false)
                    adapter.submitList(state.data)
                }
                BaseState.Status.ERROR -> {
                    load(false)
                    Timber.d(state.message)
                }
                BaseState.Status.LOADING -> {
                    load(true)

                }
            }
        }
    }

    private fun load(isRefresh: Boolean) {
        countryListSwipeRefresh.isRefreshing = isRefresh
    }

    private fun setupList() {
        rvCountries.adapter = adapter
    }

    private fun countryListClicked(countryDto: CountryDto) {
        findNavController().navigate(
            R.id.presentCountryDetail,
            bundleOf(CountryDetailFragment.COUNTRY_ID_EXTRA to countryDto.id)
        )
    }
}
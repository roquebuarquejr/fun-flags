package com.roquebuarque.architecturecomponentssample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.roquebuarque.architecturecomponentssample.R
import com.roquebuarque.architecturecomponentssample.base.BaseState
import com.roquebuarque.architecturecomponentssample.data.entities.CountryDto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_country_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class CountryListFragment : Fragment() {

    private val viewModel: CountryListViewModel by viewModels()
    private val adapter: CountryListAdapter by lazy { CountryListAdapter(::countryListClicked) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_country_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupObserver()

        countryListSwipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.country_list_title)
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
        TODO("Not yet implemented")
    }
}
package com.roquebuarque.architecturecomponentssample.ui.countrydetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.roquebuarque.architecturecomponentssample.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CountryDetailFragment: Fragment(R.layout.fragment_country_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt(COUNTRY_ID_EXTRA)?.let {
           // viewModel.start(it)
        }

        setupObserver()
    }



    private fun setupObserver() {

    }

    companion object{
        const val COUNTRY_ID_EXTRA = "COUNTRY_ID_EXTRA"
    }

}
package com.roquebuarque.architecturecomponentssample.feature.country.presentation

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.roquebuarque.architecturecomponentssample.R
import com.roquebuarque.architecturecomponentssample.feature.country.data.entities.CountryDto
import kotlinx.android.synthetic.main.item_country.view.*

class CountryListAdapter(private val listener: ((CountryDto) -> Unit)) : ListAdapter<CountryDto, CountryListAdapter.ViewHolder>(
        CountryListAdapter
) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.txtCountryNameItem
        private val capital: TextView = view.txtCapitalNameItem
        private val image: ImageView = view.imgCountryItem
        private val ctnUserList: ConstraintLayout = view.ctnMainCountryItem

        fun onBind(countryDto: CountryDto, listener: ((CountryDto) -> Unit)) {
            name.text = countryDto.name
            capital.text = countryDto.capital

            GlideToVectorYou
                .init()
                .with(image.context)
                .load(Uri.parse(countryDto.flag), image)

            ctnUserList.setOnClickListener {
                listener.invoke(countryDto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position), listener)
    }

    private companion object : DiffUtil.ItemCallback<CountryDto>() {

        override fun areItemsTheSame(oldItem: CountryDto, newItem: CountryDto): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CountryDto, newItem: CountryDto): Boolean {
            return oldItem == newItem
        }

    }
}


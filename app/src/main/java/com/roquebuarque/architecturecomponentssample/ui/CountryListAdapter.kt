package com.roquebuarque.architecturecomponentssample.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.roquebuarque.architecturecomponentssample.R
import com.roquebuarque.architecturecomponentssample.data.entities.CountryDto
import kotlinx.android.synthetic.main.item_country.view.*

class CountryListAdapter(private val listener: ((CountryDto) -> Unit)) : ListAdapter<CountryDto, CountryListAdapter.ViewHolder>(
        CountryListAdapter
) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.txtCountryNameItem
        private val capital: TextView = view.txtCapitalNameItem
        private val image: ImageView = view.imgCountryItem
        private val ctnUserList: ConstraintLayout = view.ctnMainCountryItem

        fun onBind(userDto: CountryDto, listener: ((CountryDto) -> Unit)) {
            name.text = userDto.name
            capital.text = userDto.capital
            /*Glide.with(image.context)
                    .load(userDto.flag)
                    .transform(CircleCrop())
                    .into(image)*/

            ctnUserList.setOnClickListener {
                listener.invoke(userDto)
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


package com.roquebuarque.architecturecomponentssample.feature.country.data.entities

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Keep
@Parcelize
@Entity(tableName = "countries", indices = [Index(value = ["name"], unique = true)])
data class CountryDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val capital: String,
    val region: String,
    @SerializedName("subregion")
    val subRegion: String,
    val population: Int,
    val flag: String
): Parcelable
package com.roquebuarque.architecturecomponentssample.data.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "countries")
data class CountryDto(
    @PrimaryKey
    val id: Int,
    val name: String,
    val capital: String,
    val flag: String
)
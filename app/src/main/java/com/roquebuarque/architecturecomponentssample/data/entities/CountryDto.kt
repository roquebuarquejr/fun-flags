package com.roquebuarque.architecturecomponentssample.data.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Keep
@Entity(tableName = "countries", indices = [Index(value = ["name"], unique = true)])
data class CountryDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val capital: String,
    val flag: String
)
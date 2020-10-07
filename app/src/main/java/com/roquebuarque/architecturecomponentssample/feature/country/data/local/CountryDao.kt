package com.roquebuarque.architecturecomponentssample.feature.country.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.roquebuarque.architecturecomponentssample.feature.country.data.entities.CountryDto

@Dao
interface CountryDao {

    @Query("SELECT * FROM countries ORDER BY name ASC")
    fun getAllCountries() : LiveData<List<CountryDto>>

    @Query("SELECT * FROM countries WHERE name = :name")
    fun getCountry(name: String): LiveData<CountryDto>

    @Query("SELECT * FROM countries WHERE name LIKE '%' || :name || '%' ORDER BY name ASC")
    fun getCountries(name: String):  LiveData<List<CountryDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<CountryDto>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(country: CountryDto)


}
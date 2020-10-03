package com.roquebuarque.architecturecomponentssample.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.roquebuarque.architecturecomponentssample.data.entities.CountryDto

@Dao
interface CountryDao {

    @Query("SELECT * FROM countries ORDER BY name ASC")
    fun getAllCountries() : LiveData<List<CountryDto>>

    @Query("SELECT * FROM countries WHERE id = :id")
    fun getCountry(id: Int): LiveData<CountryDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<CountryDto>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(country: CountryDto)


}
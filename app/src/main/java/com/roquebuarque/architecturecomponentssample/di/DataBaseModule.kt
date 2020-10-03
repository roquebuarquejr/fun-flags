package com.roquebuarque.architecturecomponentssample.di

import android.content.Context
import com.roquebuarque.architecturecomponentssample.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object DataBaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Provides
    fun provideCharacterDao(db: AppDatabase) = db.countryDao()
}
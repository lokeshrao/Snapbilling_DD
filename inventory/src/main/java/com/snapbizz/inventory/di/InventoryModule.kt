package com.snapbizz.inventory.di

import com.snapbizz.inventory.screen.GlobalDbRepository
import com.snapbizz.inventory.screen.IGlobalDbRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface InventoryModule {
    @Binds
    @Singleton
    fun provideGlobalDbRepository(repository: GlobalDbRepository): IGlobalDbRepository
}

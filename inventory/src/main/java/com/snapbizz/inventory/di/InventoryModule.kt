package com.snapbizz.inventory.di

import com.snapbizz.common.config.InventoryRepository
import com.snapbizz.inventory.data.InventoryRepositoryImpl
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
    fun provideInventoryRepository(repository: InventoryRepositoryImpl): InventoryRepository
}

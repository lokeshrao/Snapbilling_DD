package com.snapbizz.inventory.di

import android.content.Context
import com.snapbizz.core.database.SnapGlobalDatabase
import com.snapbizz.inventory.screen.GlobalDbRepository
import com.snapbizz.inventory.screen.IGlobalDbRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface InventoryModule {
    @Binds
    @Singleton
    fun provideGlobalDbRepository(repository: GlobalDbRepository): IGlobalDbRepository
}

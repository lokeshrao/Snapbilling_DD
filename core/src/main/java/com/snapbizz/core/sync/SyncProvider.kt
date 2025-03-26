package com.snapbizz.core.sync

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SyncProvider {

    @Provides
    @Singleton
    fun provideSyncRepo(): SyncRepository = SyncRepository()
}
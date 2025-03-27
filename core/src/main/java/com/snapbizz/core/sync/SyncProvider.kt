package com.snapbizz.core.sync

import com.snapbizz.common.models.SyncApiService
import com.snapbizz.core.datastore.SnapDataStore
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
    fun provideSyncRepo(syncApiService: SyncApiService,snapDataStore: SnapDataStore): SyncRepository = SyncRepository(syncApiService,snapDataStore)
}
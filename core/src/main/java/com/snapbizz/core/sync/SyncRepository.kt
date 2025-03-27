package com.snapbizz.core.sync

import com.snapbizz.common.models.SyncApiService
import com.snapbizz.core.datastore.SnapDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton



class SyncRepository (val syncApiService: SyncApiService,val snapDataStore: SnapDataStore) {

}
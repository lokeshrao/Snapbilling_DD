package com.snapbizz.core.datastore.di

import com.snapbizz.core.datastore.SnapDataStore
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SnapStoreProvider {
    val snapDataStore: SnapDataStore
}
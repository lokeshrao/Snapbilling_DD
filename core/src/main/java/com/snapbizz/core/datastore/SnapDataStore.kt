package com.snapbizz.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.snapstore by preferencesDataStore("snapstore")

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SnapDataStoreEntryPoint {
    val snapDataStore: SnapDataStore
}

@Singleton
class SnapDataStore @Inject constructor(@ApplicationContext context: Context) {

    private val snapstore: DataStore<Preferences> = context.snapstore

    companion object {
        private val CONFIG_KEY = stringPreferencesKey("config_json")
    }

    suspend fun saveConfig(json: String) {
        snapstore.edit { preferences ->
            preferences[CONFIG_KEY] = json
        }
    }

    fun getConfigFlow(): Flow<String?> {
        return snapstore.data.map { preferences ->
            preferences[CONFIG_KEY]
        }
    }
}

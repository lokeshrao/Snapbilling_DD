package com.snapbizz.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.snapstore by preferencesDataStore("snapstore")

object SnapDataStore {
    
    private val CONFIG_KEY = stringPreferencesKey("config_json")

    suspend fun saveConfig(context: Context, json: String) {
        context.snapstore.edit { preferences ->
            preferences[CONFIG_KEY] = json
        }
    }

    fun getConfigFlow(context: Context): Flow<String?> {
        return context.snapstore.data.map { preferences ->
            preferences[CONFIG_KEY]
        }
    }
}

package com.snapbizz.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import com.snapbizz.common.config.models.StoreDetailsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.snapstore by preferencesDataStore("snapstore")

@Singleton
class SnapDataStore @Inject constructor(@ApplicationContext context: Context) {

    private val snapstore: DataStore<Preferences> = context.snapstore

    companion object {
        private val CONFIG_KEY = stringPreferencesKey("config_json")
        private val DEVICE_ID = stringPreferencesKey("device_id")
        private val RETAILER_OWNER_NAME = stringPreferencesKey("retailer_owner_name")
        private val STORE_NAME_KEY = stringPreferencesKey("store_name_key")
        private val RETAILER_OWNER_NUMBER = longPreferencesKey("retailer_owner_number")
        private val STORE_NUMBER_KEY = longPreferencesKey("store_number_key")
        private val RETAILER_OWNER_EMAIL = stringPreferencesKey("retailer_owner_email")
        private val STORE_TIN_KEY = longPreferencesKey("store_tin_key")
        private val STORE_ADDRESS_KEY = stringPreferencesKey("store_address_key")
        private val STORE_CITY = stringPreferencesKey("store_city")
        private val STORE_STATE = stringPreferencesKey("store_state")
        private val STORE_STATE_ID = intPreferencesKey("store_state_id")
        private val STORE_ZIP = intPreferencesKey("store_zip")
        private val RETAILER_ID = longPreferencesKey("retailer_id")
        private val STORE_AUTH_KEY = stringPreferencesKey("store_auth_key")
        private val STORE_ID = longPreferencesKey("store_id")
        private val RETAILER_GSTIN = stringPreferencesKey("retailer_gstin")
        private val POS_ID = intPreferencesKey("pos_id")
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

    suspend fun setDeviceId(context: Context, deviceId: String) {
        context.snapstore.edit { preferences ->
            preferences[DEVICE_ID] = deviceId
        }
    }

    fun getDeviceId(context: Context): Flow<String?> {
        return context.snapstore.data.map { preferences ->
            preferences[DEVICE_ID]
        }
    }

    suspend fun saveStoreDetails(context: Context, storeDetails: StoreDetailsResponse?, posId: Int) {
        context.snapstore.edit { preferences ->
            storeDetails?.let { it->
                preferences[RETAILER_OWNER_NAME] = it.retailerDetails?.name.orEmpty()
                preferences[STORE_NAME_KEY] = it.storeDetails?.name.orEmpty()
                preferences[RETAILER_OWNER_EMAIL] = it.retailerDetails?.email.orEmpty()
                preferences[STORE_ADDRESS_KEY] = it.storeDetails?.address.orEmpty()
                preferences[STORE_CITY] = it.storeDetails?.city.orEmpty()
                preferences[STORE_STATE] = it.storeDetails?.state.orEmpty()
                preferences[STORE_AUTH_KEY] = it.accessToken.orEmpty()
                preferences[RETAILER_GSTIN] = it.retailerDetails?.gstin.orEmpty()

                preferences[RETAILER_OWNER_NUMBER] = it.storeDetails?.phone ?: 0L
                preferences[STORE_NUMBER_KEY] = it.storeDetails?.salespersonNumber ?: 0L
                preferences[STORE_TIN_KEY] = it.storeDetails?.tin ?: 0L
                preferences[RETAILER_ID] = it.retailerDetails?.retailerId ?: 0L
                preferences[STORE_ID] = it.storeDetails?.storeId ?: 0L

                // Store Integers
                preferences[STORE_STATE_ID] = it.storeDetails?.stateId ?: 0
                preferences[STORE_ZIP] = it.storeDetails?.pincode ?: 0
                preferences[POS_ID] = posId
            }
        }
    }
}

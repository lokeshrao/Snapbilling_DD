package com.snapbizz.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.snapbizz.common.models.AppKeysData
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import com.snapbizz.common.models.RetailerDetails
import com.snapbizz.common.models.StoreDetails
import com.snapbizz.common.models.StoreDetailsResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import com.snapbizz.core.utils.SnapPreferences
import kotlinx.coroutines.flow.first

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
        private val STORE_REGISTRATION_STATUS = booleanPreferencesKey("is_store_registered")
        private val SYNC_TOKEN = stringPreferencesKey("sync_token")
        private val TID = stringPreferencesKey("tid")
        private val MID = stringPreferencesKey("mid")
        private val APP_KEY = stringPreferencesKey("app_key")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val MERCHANT_NAME = stringPreferencesKey("merchant_name")
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

    suspend fun setDeviceId(deviceId: String) {
        snapstore.edit { preferences ->
            preferences[DEVICE_ID] = deviceId
        }
    }

    fun getDeviceId(): Flow<String?> {
        return snapstore.data.map { preferences ->
            preferences[DEVICE_ID]
        }
    }

    suspend fun saveStoreDetails(storeDetails: StoreDetailsResponse?, posId: Int) {
        snapstore.edit { preferences ->
            storeDetails?.let {
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

                preferences[STORE_STATE_ID] = it.storeDetails?.stateId ?: 0
                preferences[STORE_ZIP] = it.storeDetails?.pincode ?: 0
                preferences[POS_ID] = posId
            }
        }
    }

    fun getStoreDetails(): Flow<StoreDetailsResponse> {
        return snapstore.data.map { preferences ->
            StoreDetailsResponse(
                retailerDetails = RetailerDetails(
                    name = preferences[RETAILER_OWNER_NAME] ?: "Owner name",
                    email = preferences[RETAILER_OWNER_EMAIL] ?: "",
                    retailerId = preferences[RETAILER_ID] ?: 0L,
                    gstin = preferences[RETAILER_GSTIN] ?: ""
                ),
                storeDetails = StoreDetails(
                    name = preferences[STORE_NAME_KEY] ?: "",
                    phone = preferences[RETAILER_OWNER_NUMBER] ?: 0L,
                    salespersonNumber = preferences[STORE_NUMBER_KEY] ?: 0L,
                    tin = preferences[STORE_TIN_KEY] ?: 0L,
                    address = preferences[STORE_ADDRESS_KEY] ?: "",
                    city = preferences[STORE_CITY] ?: "",
                    state = preferences[STORE_STATE] ?: "",
                    stateId = preferences[STORE_STATE_ID] ?: 0,
                    pincode = preferences[STORE_ZIP] ?: 0,
                    storeId = preferences[STORE_ID] ?: 0L
                ),
                accessToken = preferences[STORE_AUTH_KEY] ?: "",
                posId = preferences[POS_ID]?:0
            )
        }
    }

    suspend fun setAppKeys(data: AppKeysData) {
        snapstore.edit { preferences ->
            data.let {
                preferences[TID] = it.tid.orEmpty()
                preferences[MID] = it.mid.orEmpty()
                preferences[APP_KEY] = it.appKey.orEmpty()
                preferences[USER_NAME] = it.userName.orEmpty()
                preferences[MERCHANT_NAME] = it.merchantName.orEmpty()
            }
        }
    }

    fun getAppKeys () :Flow<AppKeysData> {
        return snapstore.data.map { preferences ->
            AppKeysData(
                appKey = preferences[APP_KEY],
                mid = preferences[MID],
                tid = preferences[TID],
                userName = preferences[USER_NAME],
                merchantName = preferences[MERCHANT_NAME],
                storeId = preferences[STORE_ID]?.toInt()
            )
        }
    }


    suspend fun setStoreAsRegistered(registrationStatus: Boolean) {
        snapstore.edit { preferences ->
            preferences[STORE_REGISTRATION_STATUS] = registrationStatus
        }
    }

    suspend fun isStoreRegistrationComplete(): Boolean? {
        return snapstore.data.map { preferences ->
            preferences[STORE_REGISTRATION_STATUS]
        }.first()
    }

    suspend fun getSyncToken(): String? {
        val preferences = snapstore.data.first()
        return preferences[SYNC_TOKEN]
    }

    suspend fun setSyncToken(token: String) {
        snapstore.edit { preferences ->
            preferences[SYNC_TOKEN] = token
        }
    }

    suspend fun loadPrefs() {
        val preferences = snapstore.data.first()
        SnapPreferences.STORE_ID = preferences[STORE_ID] ?: 0L
        SnapPreferences.POS_ID = preferences[POS_ID] ?: 0
        SnapPreferences.DEVICE_ID = preferences[DEVICE_ID]?.toString().orEmpty()
        SnapPreferences.ACCESS_TOKEN = preferences[STORE_AUTH_KEY]?.toString().orEmpty()
        SnapPreferences.BILLER_NAME = preferences[RETAILER_OWNER_NAME]?.toString().orEmpty()
    }

}

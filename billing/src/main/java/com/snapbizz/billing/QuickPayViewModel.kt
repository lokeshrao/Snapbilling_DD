package com.snapbizz.billing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapbizz.core.datastore.SnapDataStore
import com.snapbizz.core.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuickPayViewModel @Inject constructor(
    private val quickPayRepository: QuickPayRepositoryImpl,
    private val dispatchers: DispatcherProvider,
    private val snapDataStore: SnapDataStore
): ViewModel() {

    fun getAppKeys() {
        viewModelScope.launch(dispatchers.io) {
            if (validateAppKeys()){
                quickPayRepository.getAppKeys().onSuccess {
                    snapDataStore.setAppKeys(it)
                }.onFailure {

                }
            }
        }
    }

    suspend fun validateAppKeys(): Boolean {
        return snapDataStore.getAppKeys().firstOrNull()?.run {
            listOf(appKey, mid, tid, userName, merchantName).all { it.isNullOrBlank().not() }
        } == true
    }


}
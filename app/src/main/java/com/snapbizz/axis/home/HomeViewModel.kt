package com.snapbizz.axis.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapbizz.core.datastore.SnapDataStore
import com.snapbizz.core.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: HomeRepositoryImpl,
    private val dispatchers: DispatcherProvider,
    private val snapDataStore: SnapDataStore
): ViewModel() {

    init {
        viewModelScope.launch(dispatchers.io) {
            snapDataStore.loadPrefs()
        }
    }

    fun getAppKeys() {
        viewModelScope.launch(dispatchers.io) {
            repo.validateAndGetAppKeys().onSuccess {

            }.onFailure {

            }
        }
    }
}
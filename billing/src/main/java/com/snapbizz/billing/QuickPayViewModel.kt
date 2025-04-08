package com.snapbizz.billing

import androidx.lifecycle.ViewModel
import com.snapbizz.core.datastore.SnapDataStore
import com.snapbizz.core.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuickPayViewModel @Inject constructor(
    private val quickPayRepository: QuickPayRepositoryImpl,
    private val dispatchers: DispatcherProvider,
    private val snapDataStore: SnapDataStore
): ViewModel() {






}
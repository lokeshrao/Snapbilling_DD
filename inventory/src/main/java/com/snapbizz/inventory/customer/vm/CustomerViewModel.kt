package com.snapbizz.inventory.customer.vm

import androidx.lifecycle.ViewModel
import com.snapbizz.core.utils.DispatcherProvider
import com.snapbizz.inventory.customer.data.CustomerRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class CustomerViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val customerRepositoryImpl: CustomerRepositoryImpl
): ViewModel() {

    private val searchQuery = MutableStateFlow("")

}
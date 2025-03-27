package com.snapbizz.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.snapbizz.common.models.ProductInfo
import com.snapbizz.core.utils.DispatcherProvider
import com.snapbizz.inventory.data.InventoryRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val inventoryRepositoryImpl: InventoryRepositoryImpl
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val searchQuery = MutableStateFlow("")

    fun insertProduct(info: ProductInfo?) {
        viewModelScope.launch(dispatcherProvider.io) {
            inventoryRepositoryImpl.addNewProducts(info).let {
                _message.value = if (it) "Added" else "Error"
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val allProducts = searchQuery.flatMapLatest { query ->
        inventoryRepositoryImpl.getAllProductDetails(query)
    }.cachedIn(viewModelScope)

    fun updateQuery(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun clearError() {
        _message.value = null
    }

    fun getInvoiceWithItems() {
        viewModelScope.launch(dispatcherProvider.io) {
            inventoryRepositoryImpl.getInvoicesWithItems()
        }
    }

}
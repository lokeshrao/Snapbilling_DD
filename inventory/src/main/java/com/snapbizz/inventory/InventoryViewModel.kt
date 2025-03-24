package com.snapbizz.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapbizz.core.utils.DispatcherProvider
import com.snapbizz.inventory.data.InventoryInfo
import com.snapbizz.inventory.data.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val inventoryRepository: InventoryRepository
): ViewModel() {

    private val _products = MutableStateFlow<InventoryInfo?>(InventoryInfo())
    val products: StateFlow<InventoryInfo?> = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun insertProduct() {
        viewModelScope.launch(dispatcherProvider.io) {
            inventoryRepository.addNewProducts(_products.value).let {
                _message.value = if (it) "Added" else "Error"
            }
        }

    }

    fun clearError() {
        _message.value = null
    }

}
package com.snapbizz.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snapbizz.core.database.entities.Inventory
import com.snapbizz.core.utils.DispatcherProvider
import com.snapbizz.inventory.data.InventoryInfo
import com.snapbizz.inventory.data.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
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
            Inventory(
                productCode = _products.value?.productCode?.value?.toLongOrNull()?:0L,
                quantity = _products.value?.quantity?.value?.toIntOrNull()?:0,
                createdAt = Date(),
                updatedAt = Date(),
                isDeleted = false,
                isSyncPending = false,
                minimumBaseQuantity = _products.value?.quantity?.value?.toLongOrNull()?:0L,
                reOrderPoint = _products.value?.reOrderPoint?.value?.toLongOrNull()?:0L
            ).let { inventory->
                inventoryRepository.addNewProducts(inventory).let {
                    _message.value = if (it>0L) "Added" else "Error"
                }
            }
        }

    }

    fun clearError() {
        _message.value = null
    }

}
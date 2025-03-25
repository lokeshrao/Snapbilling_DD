package com.snapbizz.inventory.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.snapbizz.inventory.data.InventoryRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class GlobalProductViewModel @Inject constructor(
    private val repository: InventoryRepositoryImpl
) : ViewModel() {
    private val searchQuery = MutableStateFlow("")
    @OptIn(ExperimentalCoroutinesApi::class)
    val allProducts = searchQuery.flatMapLatest { query ->
        repository.getGlobalProducts(query)
    }.cachedIn(viewModelScope)

    fun updateQuery(newQuery: String) {
        searchQuery.value = newQuery
    }
}
package com.snapbizz.common.config

sealed class SnapResult<out T> {
    data object Loading : SnapResult<Nothing>()
    data class Error(val message: String) : SnapResult<Nothing>()
    data class Success<T>(val data: T) : SnapResult<T>()
}

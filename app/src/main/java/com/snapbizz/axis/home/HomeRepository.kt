package com.snapbizz.axis.home

interface HomeRepository {
    suspend fun validateAndGetAppKeys(): Result<String>
}
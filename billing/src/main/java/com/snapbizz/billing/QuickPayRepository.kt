package com.snapbizz.billing

import com.snapbizz.common.models.AppKeysData

interface QuickPayRepository {
    fun getAppKeys(): Result<AppKeysData>
}
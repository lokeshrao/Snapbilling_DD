package com.snapbizz.billing

import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.datastore.SnapDataStore
import javax.inject.Inject

class QuickPayRepositoryImpl @Inject constructor(
    private val snapDatabase: SnapDatabase,
    private val snapDataStore: SnapDataStore
): QuickPayRepository {


}
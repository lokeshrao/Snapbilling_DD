package com.snapbizz.core.utils

import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.dao.GenericDao

data class DownSyncConfig<T, D>(
    val jsonKey: String,
    val tableName: String,
    val entityClass: Class<T>,
    val dtoClass: Class<D>,
    val daoProvider: (SnapDatabase) -> GenericDao<T>,
    val dtoToEntityMapper: ((D) -> T)?
)
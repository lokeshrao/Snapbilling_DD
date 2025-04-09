package com.snapbizz.common.models

import androidx.room.ColumnInfo

data class CustomerInfo(

    @ColumnInfo(name = "phone") var phone: Long? = null,
    @ColumnInfo(name = "name") var name: String? = null,
    @ColumnInfo(name = "address") var address: String? = null,
    @ColumnInfo(name = "email") var email: String? = null,
    @ColumnInfo(name = "creditLimit") var creditLimit: Long? = null,
    @ColumnInfo(name = "isDisabled") var isDisabled: Boolean? = null,
    @ColumnInfo(name = "isSync") var isSync: Boolean? = null,
    @ColumnInfo(name = "isUpdated") var isUpdated: Boolean? = null,
    @ColumnInfo(name = "createdAt") var createdAt: String? = null,
    @ColumnInfo(name = "updatedAt") var updatedAt: String? = null,
    @ColumnInfo(name = "gstin") var gstin: String? = null,
    @ColumnInfo(name = "altPhone") var altPhone: Long? = null,
    @ColumnInfo(name = "isAltPhoneSelected") var isAltPhoneSelected: Boolean? = null,
    @ColumnInfo(name = "isSnapOrderSync") var isSnapOrderSync: Boolean? = null

)


package com.snapbizz.inventory.customer.data

import com.snapbizz.common.models.CustomerInfo
import com.snapbizz.core.database.entities.Customer
import java.util.Date

fun convertToCustomer(customer: CustomerInfo?): Customer? {
    return try{
        Customer(
            phone = customer?.phone ?: 0,
            name = customer?.name,
            address = customer?.address,
            email = customer?.email,
            creditLimit = customer?.creditLimit,
            isDisabled = customer?.isDisabled,
            isSyncPending = customer?.isSync,
            createdAt = Date(),
            updatedAt = Date(),
            gstin = customer?.gstin,
            altPhone = customer?.altPhone,
            isAltPhoneSelected = customer?.isAltPhoneSelected,
            isSnapOrderSync = customer?.isSnapOrderSync
        )
    } catch (ex: Exception) {
        ex.printStackTrace()
        null
    }
}
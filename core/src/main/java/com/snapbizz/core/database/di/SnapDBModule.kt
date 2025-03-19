package com.snapbizz.core.database.di

import android.content.Context
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.dao.AppointmentServicesDao
import com.snapbizz.core.database.dao.AppointmentsDao
import com.snapbizz.core.database.dao.CategoryDao
import com.snapbizz.core.database.dao.CustomerDao
import com.snapbizz.core.database.dao.CustomerDetailsDao
import com.snapbizz.core.database.dao.DoctorsDao
import com.snapbizz.core.database.dao.InventoryDao
import com.snapbizz.core.database.dao.InvoiceDao
import com.snapbizz.core.database.dao.ItemsDao
import com.snapbizz.core.database.dao.LogDao
import com.snapbizz.core.database.dao.ProductCustomizationDao
import com.snapbizz.core.database.dao.ProductPacksDao
import com.snapbizz.core.database.dao.ProductsDao
import com.snapbizz.core.database.dao.RepresentativeDao
import com.snapbizz.core.database.dao.TransactionsDao
import com.snapbizz.core.database.dao.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SnapDBModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SnapDatabase {
        return SnapDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideInventoryDao(appDatabase: SnapDatabase): InventoryDao {
        return appDatabase.inventoryDao()
    }

    @Provides
    fun provideProductPacksDao(appDatabase: SnapDatabase): Lazy<ProductPacksDao?> {
        return lazy { appDatabase.productPacksDao() }
    }

    @Provides
    fun provideProductsDao(appDatabase: SnapDatabase): Lazy<ProductsDao?> {
        return lazy { appDatabase.productsDao() }
    }

    @Provides
    fun provideProductCustomizationDao(appDatabase: SnapDatabase): Lazy<ProductCustomizationDao?> {
        return lazy { appDatabase.productCustomizationDao() }
    }

    @Provides
    fun provideInvoiceDao(appDatabase: SnapDatabase): Lazy<InvoiceDao?> {
        return lazy { appDatabase.invoiceDao() }
    }

    @Provides
    fun provideItemsDao(appDatabase: SnapDatabase): Lazy<ItemsDao?> {
        return lazy { appDatabase.itemsDao() }
    }

    @Provides
    fun provideTransactionsDao(appDatabase: SnapDatabase): Lazy<TransactionsDao?> {
        return lazy { appDatabase.transactionsDao() }
    }

    @Provides
    fun provideCustomerDao(appDatabase: SnapDatabase): Lazy<CustomerDao?> {
        return lazy { appDatabase.customerDao() }
    }

    @Provides
    fun provideCustomerDetailsDao(appDatabase: SnapDatabase): Lazy<CustomerDetailsDao?> {
        return lazy { appDatabase.customerDetailsDao() }
    }

    @Provides
    fun provideUsersDao(appDatabase: SnapDatabase): Lazy<UsersDao?> {
        return lazy { appDatabase.usersDao() }
    }

    @Provides
    fun provideCategoryDao(appDatabase: SnapDatabase): Lazy<CategoryDao?> {
        return lazy { appDatabase.categoryDao() }
    }
    @Provides
    @Singleton
    fun provideLogsDao(appDatabase: SnapDatabase): LogDao {
        return appDatabase.logDao()
    }
    @Provides
    fun provideAppointmentServiceDao(appDatabase: SnapDatabase): Lazy<AppointmentServicesDao?> {
        return lazy { appDatabase.appointmentServicesDao() }
    }

    @Provides
    fun provideAppointmentsDao(appDatabase: SnapDatabase): Lazy<AppointmentsDao?> {
        return lazy { appDatabase.appointmentsDao() }
    }

    @Provides
    fun provideDoctorsDao(appDatabase: SnapDatabase): Lazy<DoctorsDao?> {
        return lazy { appDatabase.doctorsDao() }
    }

    @Provides
    fun provideRepresentativeDao(appDatabase: SnapDatabase): Lazy<RepresentativeDao?> {
        return lazy { appDatabase.representativeDao() }
    }
}

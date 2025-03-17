package com.snapbizz.core.database.di

import android.content.Context
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.dao.CategoryDao
import com.snapbizz.core.database.dao.CustomerDao
import com.snapbizz.core.database.dao.CustomerDetailsDao
import com.snapbizz.core.database.dao.InventoryDao
import com.snapbizz.core.database.dao.InvoiceDao
import com.snapbizz.core.database.dao.ItemsDao
import com.snapbizz.core.database.dao.LogDao
import com.snapbizz.core.database.dao.ProductCustomizationDao
import com.snapbizz.core.database.dao.ProductPacksDao
import com.snapbizz.core.database.dao.ProductsDao
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
    @Singleton
    fun provideProductPacksDao(appDatabase: SnapDatabase): ProductPacksDao {
        return appDatabase.productPacksDao()
    }

    @Provides
    fun provideProductsDao(appDatabase: SnapDatabase): ProductsDao {
        return appDatabase.productsDao()
    }
    @Provides
    @Singleton
    fun provideProductCustomizationDao(appDatabase: SnapDatabase): ProductCustomizationDao {
        return appDatabase.productCustomizationDao()
    }

    @Provides
    @Singleton
    fun provideInvoiceDao(appDatabase: SnapDatabase): InvoiceDao {
        return appDatabase.invoiceDao()
    }

    @Provides
    fun provideItemsDao(appDatabase: SnapDatabase): ItemsDao {
        return appDatabase.itemsDao()
    }

    @Provides
    @Singleton
    fun provideTransactionsDao(appDatabase: SnapDatabase): TransactionsDao {
        return appDatabase.transactionsDao()
    }

    @Provides
    @Singleton
    fun provideCustomerDao(appDatabase: SnapDatabase): CustomerDao {
        return appDatabase.customerDao()
    }

    @Provides
    @Singleton
    fun provideCustomerDetailsDao(appDatabase: SnapDatabase): CustomerDetailsDao {
        return appDatabase.customerDetailsDao()
    }

    @Provides
    @Singleton
    fun provideUsersDao(appDatabase: SnapDatabase): UsersDao {
        return appDatabase.usersDao()
    }
    @Provides
    @Singleton
    fun provideCategoryDao(appDatabase: SnapDatabase): CategoryDao {
        return appDatabase.categoryDao()
    }
    @Provides
    @Singleton
    fun provideLogsDao(appDatabase: SnapDatabase): LogDao {
        return appDatabase.logDao()
    }
}

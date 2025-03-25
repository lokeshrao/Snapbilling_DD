package com.snapbizz.core.database.di
import android.content.Context
import com.snapbizz.core.database.SnapDatabase
import com.snapbizz.core.database.SnapGlobalDatabase
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
object GlobalDBModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SnapGlobalDatabase {
        return SnapGlobalDatabase.getDatabase(context)
    }

//    @Provides
//    @Singleton
//    fun provideInventoryDao(appDatabase: SnapGlobalDatabase): InventoryDao {
//        return appDatabase.inventoryDao()
//    }
//
//    @Provides
//    fun provideProductPacksDao(appDatabase: SnapGlobalDatabase): Lazy<ProductPacksDao?> {
//        return lazy { appDatabase.productPacksDao() }
//    }
//
//    @Provides
//    fun provideProductsDao(appDatabase: SnapGlobalDatabase): Lazy<ProductsDao?> {
//        return lazy { appDatabase.productsDao() }
//    }
//
//    @Provides
//    fun provideProductCustomizationDao(appDatabase: SnapGlobalDatabase): Lazy<ProductCustomizationDao?> {
//        return lazy { appDatabase.productCustomizationDao() }
//    }
//
//    @Provides
//    fun provideInvoiceDao(appDatabase: SnapGlobalDatabase): Lazy<InvoiceDao?> {
//        return lazy { appDatabase.invoiceDao() }
//    }

}

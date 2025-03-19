package com.snapbizz.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.snapbizz.core.database.converters.Converters
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
import com.snapbizz.core.database.entities.AppointmentServices
import com.snapbizz.core.database.entities.Appointments
import com.snapbizz.core.database.entities.Category
import com.snapbizz.core.database.entities.Customer
import com.snapbizz.core.database.entities.CustomerDetails
import com.snapbizz.core.database.entities.Doctors
import com.snapbizz.core.database.entities.Inventory
import com.snapbizz.core.database.entities.Invoice
import com.snapbizz.core.database.entities.Items
import com.snapbizz.core.database.entities.LogEntity
import com.snapbizz.core.database.entities.ProductCustomization
import com.snapbizz.core.database.entities.ProductPacks
import com.snapbizz.core.database.entities.Products
import com.snapbizz.core.database.entities.Representative
import com.snapbizz.core.database.entities.Transactions
import com.snapbizz.core.database.entities.Users

@Database(
    entities = [Inventory::class, ProductPacks::class, Products::class, ProductCustomization::class, Invoice::class, Items::class, Customer::class, CustomerDetails::class, Users::class, Transactions::class, Category::class, LogEntity::class, AppointmentServices::class, Appointments::class, Doctors::class, Representative::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class SnapDatabase : RoomDatabase() {

    abstract fun inventoryDao(): InventoryDao
    abstract fun productPacksDao(): ProductPacksDao
    abstract fun productsDao(): ProductsDao
    abstract fun productCustomizationDao(): ProductCustomizationDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun itemsDao(): ItemsDao
    abstract fun transactionsDao(): TransactionsDao
    abstract fun customerDao(): CustomerDao
    abstract fun customerDetailsDao(): CustomerDetailsDao
    abstract fun categoryDao(): CategoryDao
    abstract fun usersDao(): UsersDao
    abstract fun logDao(): LogDao
    abstract fun appointmentsDao(): AppointmentsDao
    abstract fun doctorsDao(): DoctorsDao
    abstract fun representativeDao(): RepresentativeDao
    abstract fun appointmentServicesDao(): AppointmentServicesDao


    companion object {
        @Volatile
        private var INSTANCE: SnapDatabase? = null

        fun getDatabase(context: Context): SnapDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, SnapDatabase::class.java, "snapbizzv2.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
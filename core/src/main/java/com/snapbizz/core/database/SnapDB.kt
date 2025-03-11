//package com.snapbizz.core.database
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import androidx.room.TypeConverters
//
//
//@Database(
//    entities = [Inventory::class, ProductPacks::class, Products::class, ProductCustomization::class, Invoice::class, Items::class, Customer::class, CustomerDetails::class, Users::class, Transactions::class, Category::class],
//    version = 1
//)
//@TypeConverters(Converters::class)
//abstract class SnapDb : RoomDatabase() {
//
//    abstract fun inventoryDao(): InventoryDao
//    abstract fun productPacksDao(): ProductPacksDao
//    abstract fun productsDao(): ProductsDao
//    abstract fun productCustomizationDao(): ProductCustomizationDao
//    abstract fun invoiceDao(): InvoiceDao
//    abstract fun itemsDao(): ItemsDao
//    abstract fun transactionsDao(): TransactionsDao
//    abstract fun customerDao(): CustomerDao
//    abstract fun customerDetailsDao(): CustomerDetailsDao
//    abstract fun categoryDao(): CategoryDao
//    abstract fun usersDao(): UsersDao
//
//
//    companion object {
//        @Volatile
//        private var INSTANCE: HPHDatabase? = null
//
//        fun getDatabase(context: Context): HPHDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext, HPHDatabase::class.java, "snapbizzv2.db"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}
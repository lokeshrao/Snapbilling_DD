package com.snapbizz.core.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.snapbizz.core.database.converters.Converters
import com.snapbizz.core.database.dao.global.CategoryDao
import com.snapbizz.core.database.dao.global.GlobalProductDao
import com.snapbizz.core.database.dao.global.GstDao
import com.snapbizz.core.database.dao.global.HsnDao
import com.snapbizz.core.database.dao.global.VatDao
import com.snapbizz.core.database.entities.Category
import com.snapbizz.core.database.entities.global.Categories
import com.snapbizz.core.database.entities.global.GlobalProduct
import com.snapbizz.core.database.entities.global.Gst
import com.snapbizz.core.database.entities.global.Hsn
import com.snapbizz.core.database.entities.global.Vat

@Database(
    entities = [Categories::class, GlobalProduct::class, Gst::class, Hsn::class, Vat::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class SnapGlobalDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoryDao
    abstract fun globalProductDao(): GlobalProductDao
    abstract fun gstDao(): GstDao
    abstract fun hsnDao(): HsnDao
    abstract fun vatDao(): VatDao

    companion object {
        @Volatile
        private var instance: SnapGlobalDatabase? = null

        fun getDatabase(context: Context): SnapGlobalDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                SnapGlobalDatabase::class.java, "global.db"
            ).createFromAsset("global.db")
                .build()
    }
}

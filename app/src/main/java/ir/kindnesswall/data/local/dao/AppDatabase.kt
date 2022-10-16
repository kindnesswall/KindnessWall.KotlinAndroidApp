package ir.kindnesswall.data.local.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.kindnesswall.data.db.dao.catalog.GiftModel
import ir.kindnesswall.data.db.dao.submitrequest.RegisterGiftRequestModel
import ir.kindnesswall.data.local.dao.catalog.CatalogDao
import ir.kindnesswall.data.local.dao.charity.CharityDao
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.local.dao.province.ProvinceDao
import ir.kindnesswall.data.local.dao.province.ProvinceModel
import ir.kindnesswall.data.local.dao.submitrequest.RegisterGiftRequestDao

@Database(
    entities = [
        ProvinceModel::class,
        CharityModel::class,
        GiftModel::class,
        RegisterGiftRequestModel::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun provinceDao(): ProvinceDao
    abstract fun catalogDao(): CatalogDao
    abstract fun charityDao(): CharityDao
    abstract fun registerGiftRequestDao(): RegisterGiftRequestDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "kindness_wall.db"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }
}
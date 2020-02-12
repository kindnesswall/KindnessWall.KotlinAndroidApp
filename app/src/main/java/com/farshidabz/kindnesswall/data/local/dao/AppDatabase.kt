package com.farshidabz.kindnesswall.data.local.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.farshidabz.kindnesswall.data.local.dao.catalog.CatalogDao
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.local.dao.charity.CharityDao
import com.farshidabz.kindnesswall.data.local.dao.charity.CharityModel
import com.farshidabz.kindnesswall.data.local.dao.province.ProvinceDao
import com.farshidabz.kindnesswall.data.local.dao.province.ProvinceModel

@Database(entities = [ProvinceModel::class, CharityModel::class, GiftModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun provinceDao(): ProvinceDao
    abstract fun catalogDao(): CatalogDao
    abstract fun charityDao(): CharityDao

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
        ).allowMainThreadQueries().build()
    }
}
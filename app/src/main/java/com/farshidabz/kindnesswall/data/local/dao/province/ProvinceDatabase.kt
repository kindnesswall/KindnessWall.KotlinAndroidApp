package com.farshidabz.kindnesswall.data.local.dao.province

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProvinceModel::class], version = 1)
abstract class ProvinceDatabase : RoomDatabase() {
    abstract fun provinceDao(): ProvinceDao

    companion object {
        @Volatile
        private var instance: ProvinceDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            ProvinceDatabase::class.java, "province.db"
        ).allowMainThreadQueries().build()
    }
}
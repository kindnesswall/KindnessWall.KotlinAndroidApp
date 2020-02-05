package com.farshidabz.kindnesswall.data.local.dao.charity

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CharityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(team: List<CharityModel>)

    @Transaction
    @Query("SELECT * from charity_table")
    fun getAll(): LiveData<List<CharityModel>>
}
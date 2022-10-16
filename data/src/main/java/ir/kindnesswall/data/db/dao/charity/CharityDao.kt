package ir.kindnesswall.data.db.dao.charity

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CharityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(team: List<CharityModel>)

    @Transaction
    @Query("SELECT * from charity_table")
    fun getAll(): LiveData<List<CharityModel>>
}
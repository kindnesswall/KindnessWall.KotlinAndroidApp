package ir.kindnesswall.data.local.dao.province

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProvinceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(team: List<ProvinceModel>)

    @Transaction
    @Query("SELECT * from province_table")
    fun getAll(): LiveData<List<ProvinceModel>>
}
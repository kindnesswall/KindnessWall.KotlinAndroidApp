package ir.kindnesswall.data.local.dao.catalog

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CatalogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(team: List<GiftModel>)

    @Transaction
    @Query("SELECT * from catalog_table ORDER BY id DESC")
    fun getAll(): LiveData<List<GiftModel>>
}
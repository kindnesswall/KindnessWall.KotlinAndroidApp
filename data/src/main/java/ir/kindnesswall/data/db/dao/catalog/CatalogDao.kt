package ir.kindnesswall.data.db.dao.catalog

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CatalogDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun insert(team: List<GiftModel>)

  @Transaction
  @Query("SELECT * from catalog_table1 ORDER BY id DESC")
  fun getAll(): LiveData<List<GiftModel>>
}

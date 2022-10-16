package ir.kindnesswall.data.local.dao.submitrequest

import androidx.room.*
import ir.kindnesswall.data.db.dao.submitrequest.RegisterGiftRequestModel

@Dao
interface RegisterGiftRequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(team: RegisterGiftRequestModel)

    @Transaction
    @Query("SELECT * from register_gift_request_table")
    fun getItem(): RegisterGiftRequestModel

    @Query("DELETE FROM register_gift_request_table")
    fun delete()
}
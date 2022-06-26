package ir.kindnesswall.data.db.dao.province

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "province_table")
data class ProvinceModel(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    var name: String? = null
)
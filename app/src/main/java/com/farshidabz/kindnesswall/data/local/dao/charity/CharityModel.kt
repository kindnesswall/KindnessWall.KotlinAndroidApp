package com.farshidabz.kindnesswall.data.local.dao.charity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.farshidabz.kindnesswall.utils.helper.DataConverter
import java.util.*

@Entity(tableName = "charity_table")
@TypeConverters(DataConverter::class)
data class CharityModel(
    @PrimaryKey(autoGenerate = false)
    var id: Long = 0,
    var userId: Long = 0,
    var name: String? = null,
    var isRejected: Boolean? = null,
    var updatedAt: Date? = null,
    var createdAt: Date? = null
)
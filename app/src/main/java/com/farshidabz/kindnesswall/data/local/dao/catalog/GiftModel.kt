package com.farshidabz.kindnesswall.data.local.dao.catalog

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.utils.helper.DataConverter
import java.util.*

@Entity(tableName = "catalog_table")
@TypeConverters(DataConverter::class)
data class GiftModel(
    @PrimaryKey(autoGenerate = false)
    var id: Long = 0,

    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var cityId: Int = 0,
    var address: String? = null,
    var description: String? = null,
    var title: String? = null,
    var categoryId: Int = 0,
    var userId: Int = 0,
    var isRejected: Boolean = false,
    var isNew: Boolean = false,
    var isReviewed: Boolean = false,
    var provinceId: Int = 0,
    var giftImages: List<String>? = null,
    var price: String? = null,
    var isDeleted: Boolean = false,
    var categoryTitle: String? = null
) {
    fun getAdsTime(context: Context): String {
        return context.getString(R.string.just_now)
    }
}
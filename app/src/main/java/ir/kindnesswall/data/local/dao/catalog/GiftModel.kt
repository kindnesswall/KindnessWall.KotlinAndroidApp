package ir.kindnesswall.data.local.dao.catalog

import android.content.Context
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.github.marlonlom.utilities.timeago.TimeAgoMessages
import ir.kindnesswall.R
import ir.kindnesswall.utils.extentions.toSimpleSlashFormat
import ir.kindnesswall.utils.helper.DataConverter
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.util.*

@Entity(tableName = "catalog_table")
@TypeConverters(DataConverter::class)
@Parcelize
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
    var userId: Long = 0,
    var isRejected: Boolean = false,
    var rejectReason: String? = null,
    var isNew: Boolean = false,
    var isReviewed: Boolean = false,
    var regionId: Int? = 0,
    var regionName: String? = null,
    var provinceId: Int = 0,
    var donatedToUserId: Long? = null,
    var giftImages: List<String>? = null,
    var price: BigDecimal? = null,
    var isDeleted: Boolean = false,
    var categoryTitle: String? = null,
    var provinceName: String? = null,
    var cityName: String? = null
) : Parcelable {
    fun getAdsTime(): String {
        val messages = TimeAgoMessages.Builder().withLocale(Locale("fa")).build()

        return TimeAgo.using(updatedAt?.time ?: Date().time, messages)
    }

    fun getShortAddress(): String {
        return "$cityName،$provinceName"
    }

    fun getRegistrationTime(context: Context) =
        "${context.getString(R.string.registrationDate)}: ${createdAt?.toSimpleSlashFormat()
            ?: ""}"
}
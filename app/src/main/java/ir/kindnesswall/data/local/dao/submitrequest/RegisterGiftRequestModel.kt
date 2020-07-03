package ir.kindnesswall.data.local.dao.submitrequest

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ir.kindnesswall.utils.helper.DataConverter
import java.math.BigDecimal

@Entity(tableName = "register_gift_request_table")
@TypeConverters(DataConverter::class)
data class RegisterGiftRequestModel(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 1,
    var title: String = "",
    var description: String = "",
    var price: BigDecimal = BigDecimal.ZERO,
    var categoryId: Int = 1,
    var categoryName: String? = "",
    var isNew: Boolean = true,
    var provinceId: Int = 0,
    var provinceName: String? = "",
    var cityId: Int = 0,
    var cityName: String? = "",
    var countryId: Int = 103,
    var giftImages: ArrayList<String> = arrayListOf(),
    var isBackup: Boolean? = false
) {
    fun isEmpty(): Boolean {
        if (title.isEmpty() && description.isEmpty() && price == BigDecimal.ZERO && categoryId == 0 &&
            categoryName.isNullOrEmpty() && provinceId == 0 && provinceName.isNullOrEmpty() &&
            cityId == 0 && cityName.isNullOrEmpty() && giftImages.isEmpty()
        ) {
            return true
        }

        return false
    }
}
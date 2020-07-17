package ir.kindnesswall.data.local.dao.submitrequest

import androidx.room.TypeConverters
import ir.kindnesswall.utils.helper.DataConverter
import java.util.*

@TypeConverters(DataConverter::class)
data class RegisterCharityModel(
    var id: Long? = null,
    var userId: Long? = null,
    var name: String? = null,
    var isRejected: Boolean? = null,
    var updatedAt: Date? = null,
    var createdAt: Date? = null,
        var imageUrl: String? = null,
    var registerId: String? = null,
    var address: String? = null,
    var telephoneNumber: String? = null,
    var mobileNumber: String? = null,
    var website: String? = null,
    var email: String? = null,
    var instagram: String? = null,
    var telegram: String? = null,
    var description: String? = null
)
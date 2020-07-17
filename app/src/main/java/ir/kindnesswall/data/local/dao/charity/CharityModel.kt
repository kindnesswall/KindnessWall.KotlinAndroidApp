package ir.kindnesswall.data.local.dao.charity

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ir.kindnesswall.R
import ir.kindnesswall.utils.extentions.toSimpleSlashFormat
import ir.kindnesswall.utils.helper.DataConverter
import java.io.Serializable
import java.util.*

@Entity(tableName = "charity_table")
@TypeConverters(DataConverter::class)
data class CharityModel (
    @PrimaryKey(autoGenerate = false)
    var id: Long = 0,
    var userId: Long = 0,
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
) : Serializable {
    fun getMainOfficeAddress(context: Context): String {
        if (address.isNullOrEmpty()) return ""

        val splitAddress = address?.split("،")
        splitAddress?.let {
            if (it.isNotEmpty()) {
                return "${context.getString(R.string.central_office)}: ${it[0]}"
            }
        }

        return ""
    }

    fun getRegisterDate() = updatedAt?.toSimpleSlashFormat() ?: ""
}
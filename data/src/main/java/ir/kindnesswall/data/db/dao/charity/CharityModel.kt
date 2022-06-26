package ir.kindnesswall.data.db.dao.charity

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ir.kindnesswall.data.R
import ir.kindnesswall.data.db.DataConverter
import ir.kindnesswall.data.db.extentions.toSimpleSlashFormat
import java.io.Serializable
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
    var createdAt: Date? = null,
    var imageUrl: String?,
    var registerId: String?,
    var address: String?,
    var telephoneNumber: String?,
    var mobileNumber: String?,
    var website: String?,
    var email: String?,
    var instagram: String?,
    var telegram: String?,
    var description: String?
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
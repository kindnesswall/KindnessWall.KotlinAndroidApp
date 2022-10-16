package ir.kindnesswall.data.local

import com.chibatching.kotpref.KotprefModel
import ir.kindnesswall.data.repositories.gift.PhoneVisibility

object UserPreferences : KotprefModel() {
    override val kotprefName: String = "MyPrefers" // to be compatible with older versions

    private var _phoneVisibilityStatus by nullableStringPref(
        key = "nameKey" // to be compatible with older versions
    )
    var phoneVisibilityStatus
        get() = when (_phoneVisibilityStatus) {
            "charity" -> PhoneVisibility.JustCharities
            "all" -> PhoneVisibility.All
            "none" -> PhoneVisibility.None
            else -> null
        }
        set(value) {
            _phoneVisibilityStatus = when (value) {
                PhoneVisibility.None -> "none"
                PhoneVisibility.JustCharities -> "charity"
                PhoneVisibility.All -> "all"
                null -> null
            }
        }
}
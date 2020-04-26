
package ir.kindnesswall.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CityModel : Serializable {
    var id: Int = 0
    var name: String? = null
    var hasRegions: Boolean = false

    @SerializedName("county_id")
    var countryId: Int = 0

    @SerializedName("province_id")
    var provinceId: Int = 0
}
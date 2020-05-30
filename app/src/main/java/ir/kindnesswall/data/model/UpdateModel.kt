package ir.kindnesswall.data.model

import com.google.gson.annotations.SerializedName

class UpdateModel {
    var platform: String? = null

    @SerializedName("requiredVersionCode")
    var minVersion: Int = 1

    @SerializedName("availableVersionCode")
    var currentVersion: Int = 1

    @SerializedName("requiredVersionName")
    var minVersionName: String? = null

    @SerializedName("availableVersionName")
    var currentVersionName: String? = null
}
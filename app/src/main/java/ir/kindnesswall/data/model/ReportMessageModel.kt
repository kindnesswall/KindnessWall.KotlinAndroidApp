package ir.kindnesswall.data.model

import com.google.gson.annotations.SerializedName

data class ReportMessageModel(
    @SerializedName("charityId")
    var charityId: Long,
    @SerializedName("message")
    var message: String
    )
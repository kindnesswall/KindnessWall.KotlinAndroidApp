package ir.kindnesswall.data.model

import com.google.gson.annotations.SerializedName

data class ReportCharityMessageModel(
    @SerializedName("charityId")
    var charityId: Long,
    @SerializedName("message")
    var message: String
)
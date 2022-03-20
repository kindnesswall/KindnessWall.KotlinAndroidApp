package ir.kindnesswall.data.model

import com.google.gson.annotations.SerializedName


data class ReportGiftMessageModel(
    @SerializedName("giftId")
    var giftId: Long,
    @SerializedName("message")
    var message: String
)
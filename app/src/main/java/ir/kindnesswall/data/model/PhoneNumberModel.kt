package ir.kindnesswall.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PhoneNumberModel {

    @SerializedName("phoneNumber")
    @Expose
    val phoneNumber : String ?= null
}
package ir.kindnesswall.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProfilePhoneModel(
    @SerializedName("isCharity")
    @Expose
    val isCharity : Boolean ,
    @SerializedName("phoneNumber")
    @Expose
    val phoneNumber : String,
    @SerializedName("isSupporter")
    @Expose
    val isSupporter : Boolean,
    @SerializedName("id")
    @Expose
    val id : Int
)
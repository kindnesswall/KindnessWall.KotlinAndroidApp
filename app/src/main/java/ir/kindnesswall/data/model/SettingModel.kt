package ir.kindnesswall.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SettingModel {
    @SerializedName("setting")
    @Expose
    var setting : String ?= null
}
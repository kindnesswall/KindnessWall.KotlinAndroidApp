package ir.kindnesswall.view.main.reviewcharity.model

import com.google.gson.annotations.SerializedName
import ir.kindnesswall.data.local.dao.charity.CharityModel

/**
 * Created by Javad Vatan on 6/12/2020
 * Sites: http://Jvatan.ir && http://JavadVatan.ir
 */
data class CharityAndStatusModel(@SerializedName("status") val status: CharityStatus,
                                 @SerializedName("charity") val charity: CharityModel?)

enum class CharityStatus{
    notRequested,pending,rejected,isCharity
}
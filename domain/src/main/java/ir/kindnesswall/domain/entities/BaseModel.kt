package ir.kindnesswall.domain.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class BaseModel : Serializable {
  @Transient
  var statusCode = 200

  @SerializedName("error")
  var isFailed = false

  @SerializedName("reason")
  var apiResponseMessage = ""
}
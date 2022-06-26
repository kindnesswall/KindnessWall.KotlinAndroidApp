package ir.kindnesswall.domain.entities

data class PushRegisterRequestModel(
  var devicePushToken: String,
  var type: String = "Firebase"
)
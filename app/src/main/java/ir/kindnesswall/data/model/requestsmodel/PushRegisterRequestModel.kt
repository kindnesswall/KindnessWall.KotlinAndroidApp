package ir.kindnesswall.data.model.requestsmodel

data class PushRegisterRequestModel(
    var deviceIdentifier: String,
    var devicePushToken: String,
    var type: String = "Firebase"
)
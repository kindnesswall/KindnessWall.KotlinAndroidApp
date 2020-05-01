package ir.kindnesswall.data.model.requestsmodel

data class PushRegisterRequestModel(
    var devicePushToken: String,
    var type: String = "Firebase"
)
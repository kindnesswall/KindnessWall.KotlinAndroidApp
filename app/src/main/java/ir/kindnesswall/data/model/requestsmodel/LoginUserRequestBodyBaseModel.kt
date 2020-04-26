package ir.kindnesswall.data.model.requestsmodel

data class LoginUserRequestBodyBaseModel(var phoneNumber: String = "", var activationCode: String = "") : RequestBaseModel()
package ir.kindnesswall.domain.entities

data class LoginUserRequestBodyBaseModel(
  var phoneNumber: String = "",
  var activationCode: String = ""
) : RequestBaseModel()

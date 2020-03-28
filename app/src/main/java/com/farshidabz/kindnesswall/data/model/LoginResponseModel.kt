package com.farshidabz.kindnesswall.data.model

class LoginResponseModel : BaseModel() {
    var isAdmin: Boolean = false
    var isCharity: Boolean = false
    var token: Token? = null
}

class Token : BaseModel() {
    var userID: Long = 0
    var token: String? = null
    var id: Long = 0
}
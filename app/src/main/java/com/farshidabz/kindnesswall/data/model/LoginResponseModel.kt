package com.farshidabz.kindnesswall.data.model

class LoginResponseModel {
    var isAdmin: Boolean = false
    var isCharity: Boolean = false
    var token: Token? = null
}

class Token {
    var userID: Long = 0
    var token: String? = null
    var id: Long = 0
}
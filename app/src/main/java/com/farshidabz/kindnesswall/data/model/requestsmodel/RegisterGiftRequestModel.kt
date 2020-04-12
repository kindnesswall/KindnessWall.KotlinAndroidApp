package com.farshidabz.kindnesswall.data.model.requestsmodel

import com.farshidabz.kindnesswall.data.local.AppPref

class RegisterGiftRequestModel {
    var title: String = ""
    var description: String = ""
    var price = 0
    var giftImages = arrayListOf<String>()

    var categoryId = 1
    var isNew = true
    var provinceId = 0
    var cityId = 0

    var countryId = AppPref.countryId
}
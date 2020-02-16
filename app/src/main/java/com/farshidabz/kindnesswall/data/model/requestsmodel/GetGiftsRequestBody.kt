package com.farshidabz.kindnesswall.data.model.requestsmodel

class GetGiftsRequestBody {
    var beforeId: Long = Long.MAX_VALUE
    var count: Int = 50
    var categoryIds: Int? = null
    var provinceId: Int? = null
    var cityId: Int? = null
    var regionIds: Int? = null
    var searchWord: String? = null
}
package com.farshidabz.kindnesswall.data.model.gift

import java.util.*

class GiftModel {
    var cityId: Int = 0
    var address: String? = null
    var description: String? = null
    var title: String? = null
    var categoryId: Int = 0
    var userId: Int = 0
    var isRejected: Boolean = false
    var id: Int = 0
    var isNew: Boolean = false
    var isReviewed: Boolean = false
    var createdAt: Date? = null
    var provinceId: Int = 0
    var giftImages: List<Any>? = null
    var updatedAt: Date? = null
    var price: String? = null
    var isDeleted: Boolean = false
    var categoryTitle: String? = null
}
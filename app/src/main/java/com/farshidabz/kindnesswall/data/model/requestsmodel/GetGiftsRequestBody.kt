package com.farshidabz.kindnesswall.data.model.requestsmodel

data class GetGiftsRequestBody(var beforeId: Long = Long.MAX_VALUE, var count: Int = 50)
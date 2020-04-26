package ir.kindnesswall.data.model.requestsmodel

class GetGiftsRequestBaseBody : RequestBaseModel() {
    var beforeId: Long = Long.MAX_VALUE
    var count: Int = 50
    var categoryIds: ArrayList<Int>? = null
    var provinceId: Int? = null
    var cityId: Int? = null
    var regionIds: Int? = null
    var searchWord: String? = null

    fun clear() {
        beforeId = Long.MAX_VALUE
        count = 50

        categoryIds = null

        provinceId = null
        cityId = null
        regionIds = null
        searchWord = null
    }
}
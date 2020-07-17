package ir.kindnesswall.data.model.requestsmodel

class GetUsersRequestModel  {
    var beforeId: Long? = Long.MAX_VALUE
    var count: Int? = 20
    val phoneNumber: String = "98"

    fun clear() {
        beforeId = Long.MAX_VALUE
        count = 20
    }
}
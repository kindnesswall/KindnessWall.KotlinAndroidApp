package ir.kindnesswall.utils

import ir.kindnesswall.annotation.NetworkResponseType
import ir.kindnesswall.data.model.BaseModel
import retrofit2.Response

/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

fun buildAMessage(@NetworkResponseType networkResponseType: Int, networkResponse: Response<*>?):
        String {

    return if (networkResponse?.body() != null) {
        (networkResponse.body() as BaseModel?)?.apiResponseMessage ?: ""
    } else {
        ""
    }
}
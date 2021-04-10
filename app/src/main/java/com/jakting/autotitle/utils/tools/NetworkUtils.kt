package com.jakting.autotitle.utils.tools

import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.HttpException
import java.security.MessageDigest


fun getErrorString(t: Throwable): String {
    return (t as HttpException).response()?.errorBody()?.string().let { return@let it.toString() }
}

fun getErrorStatusCode(t: Throwable): Int {
    return (t as HttpException).code()
}

fun getPostBody(jsonForPost: String): RequestBody {
    return RequestBody.create(
        MediaType.parse("application/json"), jsonForPost
    )
}

fun md5(content: String): String {
    val hash = MessageDigest.getInstance("MD5").digest(content.toByteArray())
    val hex = StringBuilder(hash.size * 2)
    for (b in hash) {
        var str = Integer.toHexString(b.toInt())
        if (b < 0x10) {
            str = "0$str"
        }
        hex.append(str.substring(str.length - 2))
    }
    return hex.toString()
}

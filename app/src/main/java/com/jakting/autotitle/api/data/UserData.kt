package com.jakting.autotitle.api.data


data class TokenBody(
    var access_token: String,
    var refresh_token: String
)

data class AccessTokenBody(
    var access_token: String
)

data class UserInfo(
    var id: String,
    var username: String,
    var nickname: String,
    var email: String,
    var avatar: String,
    var sex: String,
    var introduction: String,
    var city: MutableList<String>
)

data class UserInfoUpdate(
    var msg: String,
    var result: UserInfoUpdateBody
)

data class UserInfoUpdateBody(
    var nickname: Int,
    var sex: Int,
    var introduction: Int,
    var city: Int
)

data class UserInfoForUpdate(
    var nickname: String,
    var sex: String,
    var introduction: String,
    var city: MutableList<String>
)

data class UserAvatarBody(
    val filename: String,
    val message: String,
    val time: Double,
    val avatar: String
)


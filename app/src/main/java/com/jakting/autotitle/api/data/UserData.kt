package com.jakting.autotitle.api.data


data class TokenBody(
    var access_token: String,
    var refresh_token: String
)

data class AccessTokenBody(
    var access_token: String
)

data class UserInfo(
    var nickname: String,
    var username: String,
    var email: String
)

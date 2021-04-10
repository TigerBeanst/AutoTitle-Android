package com.jakting.autotitle.api.data


data class TokenBody(
    var access_token: String,
    var token_type: String
)

data class UserInfo(
    var nickname: String,
    var username: String,
    var email: String
)

data class LoginStatus(
    var username: String,
    var password: String
)

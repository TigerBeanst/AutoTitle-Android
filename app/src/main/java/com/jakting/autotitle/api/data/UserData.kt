package com.jakting.autotitle.api.data


data class TokenBody(
    val access_token: String,
    val token_type: String
)

data class UserInfo(
    val nickname: String,
    val username: String,
    val email: String
)

data class LoginStatus(
    val isLogin: Boolean,
    val username: String,
    val password: String
)

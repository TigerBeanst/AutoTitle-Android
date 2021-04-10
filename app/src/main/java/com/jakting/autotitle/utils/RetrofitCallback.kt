package com.jakting.autotitle.utils

interface RetrofitCallback {
    fun onSuccess(value: Any)
    fun onError(t: Throwable)
}
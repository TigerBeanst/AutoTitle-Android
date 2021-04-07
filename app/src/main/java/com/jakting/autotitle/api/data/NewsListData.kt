package com.jakting.autotitle.api.data

data class NewObject(
    val title: String,
    val time: String,
    val src: String,
    val category: String,
    val pic: String,
    val content: String,
    val url: String,
    val weburl: String,
    val timestamp: Int
)

data class NewList(
    val channel:String,
    val num:Int,
    val list: List<NewObject>
)

data class NewResult(
    val status:Int,
    val msg:String,
    val list: List<NewList>
)

data class News(
    val code: String,
    val charge: String,
    val msg: String,
    val requestId: String,
    val result: List<NewResult>
)
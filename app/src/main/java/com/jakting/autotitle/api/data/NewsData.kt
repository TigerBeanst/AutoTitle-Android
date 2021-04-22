package com.jakting.autotitle.api.data

data class AutoTitleObject(
    val result: String,
    val original_length: Int,
    val result_length: Int
)

data class NewObject(
    var title: String,
    var time: String,
    var src: String,
    var category: String,
    var pic: String,
    var content: String,
    var url: String,
    var weburl: String,
    var timestamp: Int
)

data class NewList(
    var channel: String,
    var num: Int,
    var list: List<NewObject>
)

data class NewResult(
    var status: Int,
    var msg: String,
    var result: NewList
)

data class NewSearchObject(
    var title: String,
    var time: String,
    var src: String,
    var pic: String,
    var url: String,
    var weburl: String,
    var content: String,
    var gallery: String,
    var addtime: Int
)

data class NewSearchList(
    var keyword: String,
    var num: Int,
    var list: List<NewSearchObject>
)

data class NewSearchResult(
    var status: Int,
    var msg: String,
    var result: NewSearchList
)

data class News(
    var code: String,
    var charge: String,
    var msg: String,
    var requestId: String,
    var result: NewResult
)
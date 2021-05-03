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

data class News(
    var code: String,
    var charge: String,
    var msg: String,
    var requestId: String,
    var result: NewResult
)

data class NewsSearch(
    val charge: Boolean,
    val code: String,
    val msg: String,
    val requestId: String,
    val result: NewsSearchResult
)

data class NewsSearchResult(
    val msg: String,
    val result: NewsSearchResultResult,
    val status: Int
)

data class NewsSearchResultResult(
    val keyword: String,
    val list: List<NewsSearchResultResultObject>,
    val num: Int
)

data class NewsSearchResultResultObject(
    val addtime: Int,
    val category: String,
    val content: String,
    val gallery: String,
    val pic: String,
    val src: String,
    val time: String,
    val title: String,
    val url: String,
    val weburl: String,
    var autotitle: String
)
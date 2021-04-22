package com.jakting.autotitle.utils.tools

import android.content.Context
import java.util.regex.Matcher
import java.util.regex.Pattern

const val API_BASE_URL = "http://192.168.1.100:8000/"

fun Context.dip2px(dp: Float): Float {
    val density: Float = this.resources.displayMetrics.density // 密度
    return dp * density
}

fun delHTMLTag(htmlStr: String): String {
    var htmlStr = htmlStr
    val regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>" //定义script的正则表达式
    val regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>" //定义style的正则表达式
    val regEx_html = "<[^>]+>" //定义HTML标签的正则表达式
    val p_script: Pattern = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE)
    val m_script: Matcher = p_script.matcher(htmlStr)
    htmlStr = m_script.replaceAll("") //过滤script标签
    val p_style: Pattern = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE)
    val m_style: Matcher = p_style.matcher(htmlStr)
    htmlStr = m_style.replaceAll("") //过滤style标签
    val p_html: Pattern = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE)
    val m_html: Matcher = p_html.matcher(htmlStr)
    htmlStr = m_html.replaceAll("") //过滤html标签
    return htmlStr.trim { it <= ' ' } //返回文本字符串
}

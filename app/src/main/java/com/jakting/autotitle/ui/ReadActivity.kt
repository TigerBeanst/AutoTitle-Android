package com.jakting.autotitle.ui

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.jakting.autotitle.R
import com.jakting.autotitle.api.data.AutoTitleObject
import com.jakting.autotitle.api.data.NewObject
import com.jakting.autotitle.api.parse.getAutoTitleMethod
import com.jakting.autotitle.utils.RetrofitCallback
import com.jakting.autotitle.utils.tools.GlideImageGetter
import com.jakting.autotitle.utils.tools.delHTMLTag
import com.jakting.autotitle.utils.tools.getErrorString
import com.jakting.autotitle.utils.tools.logd
import kotlinx.android.synthetic.main.activity_read.*


class ReadActivity : AppCompatActivity() {

    lateinit var newObject: NewObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        newObject = Gson().fromJson(intent.getStringExtra("newObject"), NewObject::class.java)
        initView()
        initHtml()

    }

    private fun initHtml() {


    }

    private fun initView() {
        read_autotitle.text = "生成中……"
        read_title.text = newObject.title
        read_author_time.text = "${newObject.src}·${newObject.time}"
        print(newObject.content)
        read_content.text = Html.fromHtml(newObject.content, GlideImageGetter(read_content), null)
        val cleanContent = delHTMLTag(newObject.content)
//        logd(cleanContent)
        requestAutoTitle(cleanContent)
    }


    private fun requestAutoTitle(content: String) {
        getAutoTitleMethod(content, object : RetrofitCallback {
            override fun onSuccess(value: Any) {
                val autoTitleObject = value as AutoTitleObject
                read_autotitle.text = autoTitleObject.result
//                read_content.text = HtmlSpanner(read_content.currentTextColor, read_content.textSize).fromHtml(newObject.content)
            }

            override fun onError(t: Throwable) {
                getErrorString(t)
            }

        })
    }

}
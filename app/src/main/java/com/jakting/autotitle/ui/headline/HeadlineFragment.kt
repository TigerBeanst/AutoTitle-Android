package com.jakting.autotitle.ui.headline

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.google.gson.Gson
import com.jakting.autotitle.R
import com.jakting.autotitle.api.data.NewObject
import com.jakting.autotitle.api.data.News
import com.jakting.autotitle.api.data.AccessTokenBody
import com.jakting.autotitle.api.parse.getNewsListMethod
import com.jakting.autotitle.api.parse.getAccessTokenMethod
import com.jakting.autotitle.ui.ReadActivity
import com.jakting.autotitle.utils.MyApplication.Companion.tokenBody
import com.jakting.autotitle.utils.RetrofitCallback
import com.jakting.autotitle.utils.tools.getErrorStatusCode
import com.jakting.autotitle.utils.tools.logd
import kotlinx.android.synthetic.main.fragment_headline.*

class HeadlineFragment : Fragment() {

    var start = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_headline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        headline_refreshLayout.onRefresh {
            start = 0
            logd("进入 onRefresh，此时start为$start")
            checkAccessToken("headline", 0, 10)
        }.autoRefresh()

        headline_refreshLayout.onLoadMore {
            start++
            logd("进入 onLoadMore，此时start为$start")
            checkAccessToken("headline", start * 10, 10)
        }.setEnableLoadMore(true).setEnableAutoLoadMore(true)

        headline_recyclerView
            .linear()
            .setup {
                addType<NewObject>(R.layout.item_new_object)
                onBind {
                    val newBackground = findView<ImageView>(R.id.new_background)
                    val newTextLine = findView<LinearLayout>(R.id.new_text_line)
                    val newTextSrc = findView<TextView>(R.id.new_text_src)
                    newTextSrc.post {
                        val layoutParams: ViewGroup.LayoutParams = newTextLine.layoutParams
                        layoutParams.width = newTextSrc.width
                        newTextLine.layoutParams = layoutParams
                    }
                    Glide.with(requireActivity())
                        .load(getModel<NewObject>().pic)
                        .centerCrop()
                        .into(newBackground)
                }
                onClick(R.id.new_layout) {
                    when (it) {
                        R.id.new_layout -> {
                            val intent = Intent(requireActivity(), ReadActivity::class.java)
                            intent.putExtra("newObject", Gson().toJson(getModel<NewObject>()))
                            startActivity(intent)
                        }
                    }
                }
            }

    }

    private fun checkAccessToken(kind: String, start: Int, count: Int) {
        if (tokenBody.access_token == "") {
            logd("此时 access_token 为空")
            getAccessTokenMethod(object : RetrofitCallback {
                override fun onSuccess(value: Any) {
                    val accessTokenBody = value as AccessTokenBody
                    tokenBody.access_token = accessTokenBody.access_token
                    logd("所返回的token为：${accessTokenBody.access_token}")
                    requestNewsList(kind, start, count)
                }

                override fun onError(t: Throwable) {

                }

            })
        } else {
            requestNewsList(kind, start, count)
        }
    }

    private fun requestNewsList(kind: String, start: Int, count: Int) {
        getNewsListMethod(kind, start, count, object : RetrofitCallback {
            override fun onSuccess(value: Any) {
                val newList = (value as News).result.result.list as ArrayList<NewObject>
                if (start == 0) {
                    headline_recyclerView.models = newList
                } else {
                    headline_refreshLayout.addData(newList)
                }

                headline_refreshLayout.finishRefresh()
            }

            override fun onError(t: Throwable) {
                logd("错误码是${getErrorStatusCode(t)}")
                if (getErrorStatusCode(t) == 401) {
                    //access_token炸了
                    getAccessTokenMethod(object : RetrofitCallback {
                        override fun onSuccess(value: Any) {
                            val refreshTokenBody = value as AccessTokenBody
                            tokenBody.access_token = refreshTokenBody.access_token
                        }

                        override fun onError(t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }

        })
    }


}
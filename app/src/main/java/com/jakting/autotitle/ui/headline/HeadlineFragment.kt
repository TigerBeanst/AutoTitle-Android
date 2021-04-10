package com.jakting.autotitle.ui.headline

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.google.gson.Gson
import com.jakting.autotitle.R
import com.jakting.autotitle.api.data.NewObject
import com.jakting.autotitle.api.data.News
import com.jakting.autotitle.api.parse.getNewsListMethod
import com.jakting.autotitle.ui.ReadActivity
import com.jakting.autotitle.utils.RetrofitCallback
import com.jakting.autotitle.utils.tools.getErrorString
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        refreshLayout_headline.onRefresh {
            start = 0
            logd("进入 onRefresh，此时start为$start")
            requestNewsList("headline", 0, 10)
        }.autoRefresh()

        refreshLayout_headline.onLoadMore {
            start++
            logd("进入 onLoadMore，此时start为$start")
            requestNewsList("headline", start * 10, 10)
        }.setEnableLoadMore(true).setEnableAutoLoadMore(true)

        recycler_headline.linear().setup {
            addType<NewObject>(R.layout.item_new_object)
            onBind {
                val itemImg = findView<ImageView>(R.id.item_img)
                Glide.with(requireActivity()).load(getModel<NewObject>().pic).into(itemImg)
            }
            onClick(R.id.news_list_item) {
                when(it){
                    R.id.news_list_item -> {
                        val intent = Intent(requireActivity(), ReadActivity::class.java)
                        intent.putExtra("newObject", Gson().toJson(getModel<NewObject>()))
                        startActivity(intent)
                    }
                }
            }
        }

    }

    private fun requestNewsList(kind: String, start: Int, count: Int) {
        getNewsListMethod(kind, start, count, object : RetrofitCallback {
            override fun onSuccess(value: Any) {
                val newList = (value as News).result.result.list as ArrayList<NewObject>
                if (start == 0) {
                    recycler_headline.models = newList
                } else {
                    refreshLayout_headline.addData(newList)
                }

                refreshLayout_headline.finishRefresh()
            }

            override fun onError(t: Throwable) {
                getErrorString(t)
            }

        })
    }


}
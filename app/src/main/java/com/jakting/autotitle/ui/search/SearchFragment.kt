package com.jakting.autotitle.ui.search

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
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.ferfalk.simplesearchview.SimpleSearchView
import com.google.gson.Gson
import com.jakting.autotitle.R
import com.jakting.autotitle.api.data.AutoTitleObject
import com.jakting.autotitle.api.data.NewObject
import com.jakting.autotitle.api.data.NewsSearch
import com.jakting.autotitle.api.data.NewsSearchResultResultObject
import com.jakting.autotitle.api.parse.getAccessTokenMethod
import com.jakting.autotitle.api.parse.getAutoTitleMethod
import com.jakting.autotitle.api.parse.searchNewsListMethod
import com.jakting.autotitle.ui.ReadActivity
import com.jakting.autotitle.utils.MyApplication.Companion.tokenBody
import com.jakting.autotitle.utils.RetrofitCallback
import com.jakting.autotitle.utils.tools.delHTMLTag
import com.jakting.autotitle.utils.tools.getErrorString
import com.jakting.autotitle.utils.tools.logd
import kotlinx.android.synthetic.main.activity_read.*
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    var start = 0
    var keyword = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initToolbar()
        search_refreshLayout.onRefresh {
            start = 0
            logd("进入 onRefresh，此时start为$start")
            checkAccessToken()
        }.setEnableRefresh(false)


        search_recyclerView
            .linear()
            .setup {
                addType<NewsSearchResultResultObject>(R.layout.item_search_object)
                onBind {
                    val searchTitle = findView<TextView>(R.id.search_text_title)
                    val searchBackground = findView<ImageView>(R.id.search_background)
                    val searchTextLine = findView<LinearLayout>(R.id.search_text_line)
                    val searchTextSrc = findView<TextView>(R.id.search_text_src)
                    val searchAutoTitle = findView<TextView>(R.id.search_text_autotitle)
                    searchTitle.text = getModel<NewsSearchResultResultObject>().title
                    if(getModel<NewsSearchResultResultObject>().autotitle!=""){
                        searchAutoTitle.text = getModel<NewsSearchResultResultObject>().autotitle
                    }
                    searchTextSrc.text = getModel<NewsSearchResultResultObject>().src
                    searchTextLine.post {
                        val layoutParams: ViewGroup.LayoutParams = searchTextLine.layoutParams
                        layoutParams.width = searchTextSrc.width
                        searchTextLine.layoutParams = layoutParams
                    }
                    Glide.with(requireActivity())
                        .load(getModel<NewsSearchResultResultObject>().pic)
                        .centerCrop()
                        .into(searchBackground)
                }
                onClick(R.id.search_layout) {
                    when (it) {
                        R.id.search_layout -> {
                            val intent = Intent(requireActivity(), ReadActivity::class.java)
                            intent.putExtra("newObject", Gson().toJson(getModel<NewObject>()))
                            startActivity(intent)
                        }
                    }
                }
            }

    }

    private fun initToolbar() {
        search_toolbar.inflateMenu(R.menu.toolbar_search_menu)
        search_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.search_toolbar_search -> {
                    search_searchView.showSearch()
                    true
                }
                else -> false
            }
        }
        search_searchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }

            override fun onQueryTextCleared(): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                keyword = query
                search_toolbar.title =
                    query + " " + requireActivity().getString(R.string.search_main_text_title_result)
                search_refreshLayout.autoRefresh()
                search_refreshLayout.setEnableRefresh(true)
                return true
            }
        })
    }

    private fun checkAccessToken() {
        if (tokenBody.access_token == "") {
            logd("此时 access_token 为空")
            getAccessTokenMethod(object : RetrofitCallback {
                override fun onSuccess(value: Any) {
                    requestSearchNewsList(keyword)
                }

                override fun onError(t: Throwable) {}

            })
        } else {
            requestSearchNewsList(keyword)
        }
    }

    private fun requestSearchNewsList(keyword: String) {
        searchNewsListMethod(keyword, object : RetrofitCallback {
            override fun onSuccess(value: Any) {
                val newsSearchList =
                    (value as NewsSearch).result.result.list as ArrayList<NewsSearchResultResultObject>
                search_recyclerView.models = newsSearchList
                search_refreshLayout.finishRefresh()
                requestAutoTitleSearch(newsSearchList)
            }

            override fun onError(t: Throwable) {

            }

        })
    }

    private fun requestAutoTitleSearch(newsSearchList: ArrayList<NewsSearchResultResultObject>) {
        newsSearchList.forEach {
            val cleanContent = delHTMLTag(it.content)
            getAutoTitleMethod(cleanContent, object : RetrofitCallback {
                override fun onSuccess(value: Any) {
                    val autoTitleObject = value as AutoTitleObject
                    it.autotitle = autoTitleObject.result
                    search_recyclerView.models = newsSearchList
                }

                override fun onError(t: Throwable) {
                    getErrorString(t)
                }

            })
        }
    }
}
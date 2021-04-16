package com.jakting.autotitle.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.jakting.autotitle.R
import com.jakting.autotitle.api.data.NewObject
import com.jakting.autotitle.api.data.News
import com.jakting.autotitle.api.parse.getNewsListMethod
import com.jakting.autotitle.utils.RetrofitCallback
import com.jakting.autotitle.utils.tools.getErrorString
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getNewsListMethod("science", 0, 10, object : RetrofitCallback {
            override fun onSuccess(value: Any) {
                val news = value as News
                recycler_category.linear().setup {
                    addType<NewObject>(R.layout.item_new_object)
                    onBind {
                        val newBackground = findView<ImageView>(R.id.new_background)
                        Glide.with(requireActivity())
                            .load(getModel<NewObject>().pic)
                            .into(newBackground)
                    }
                }.models = news.result.result.list
            }

            override fun onError(t: Throwable) {
                getErrorString(t)
            }

        })


    }

}
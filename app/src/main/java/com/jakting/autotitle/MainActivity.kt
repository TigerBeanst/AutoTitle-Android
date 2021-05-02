package com.jakting.autotitle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jakting.autotitle.api.data.UserInfo
import com.jakting.autotitle.api.parse.getUserInfoMethod
import com.jakting.autotitle.ui.category.CategoryFragment
import com.jakting.autotitle.ui.headline.HeadlineFragment
import com.jakting.autotitle.ui.my.MyFragment
import com.jakting.autotitle.utils.MyApplication.Companion.sp
import com.jakting.autotitle.utils.MyApplication.Companion.tokenBody
import com.jakting.autotitle.utils.MyApplication.Companion.userInfo
import com.jakting.autotitle.utils.RetrofitCallback
import com.jakting.autotitle.utils.tools.logd
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private val headlineFragment: HeadlineFragment = HeadlineFragment()
    private val categoryFragment: CategoryFragment = CategoryFragment()
    private val myFragment: MyFragment = MyFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenBody.refresh_token = sp.getString("refresh_token", "").toString()
        setContentView(R.layout.activity_main)
        viewPaper.addOnPageChangeListener(this)
        nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        viewPaper.adapter = object :
            FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                when (position) {
                    0 -> {
                        logd("headlineFragment")
                        return headlineFragment
                    }
                    1 -> {
                        logd("categoryFragment")
                        return categoryFragment
                    }
                    2 -> {
                        logd("myFragment")
                        return myFragment
                    }
                    else -> {
                        return headlineFragment
                    }
                }

            }

            override fun getCount(): Int {
                return 3
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item -> //点击BottomNavigationView的Item项，切换ViewPager页面
            //menu/navigation.xml里加的android:orderInCategory属性就是下面item.getOrder()取的值
            logd("item.order=======${item.order}")
            viewPaper.currentItem = item.order
            true
        }

    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
        logd("position=======${position}")
    }

    override fun onPageSelected(position: Int) { //页面滑动的时候，改变BottomNavigationView的Item高亮
        nav_view.menu.getItem(position).isChecked = true
    }

    override fun onPageScrollStateChanged(state: Int) {}
}
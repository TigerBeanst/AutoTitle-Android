package com.jakting.autotitle.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.slider.Slider
import com.google.gson.Gson
import com.jakting.autotitle.R
import com.jakting.autotitle.api.data.AutoTitleObject
import com.jakting.autotitle.api.data.NewObject
import com.jakting.autotitle.api.parse.getAutoTitleMethod
import com.jakting.autotitle.utils.RetrofitCallback
import com.jakting.autotitle.utils.tools.*
import com.zqc.opencc.android.lib.ChineseConverter
import com.zqc.opencc.android.lib.ConversionType
import kotlinx.android.synthetic.main.activity_read.*
import kotlinx.android.synthetic.main.layout_sheet_read.view.*


class ReadActivity : AppCompatActivity() {

    lateinit var newObject: NewObject
    var openCC = "sc"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        setSupportActionBar(findViewById(R.id.toolbar))
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = getString(R.string.read_autotitle_actionbar_title)
            toolbar.setNavigationOnClickListener {

            }
        }
        newObject = Gson().fromJson(intent.getStringExtra("newObject"), NewObject::class.java)
        initView()
        initHtml()

    }

    private fun initHtml() {


    }

    private fun initView() {
        read_autotitle.text = "生成中……"
        read_title.text = newObject.title
        read_text_time.text = newObject.time
        logd("1read_text_src.width为${read_text_src.width}")
        read_text_src.text = newObject.src
        read_text_src.post {
            val layoutParams: ViewGroup.LayoutParams = read_text_line.layoutParams
            logd("2read_text_src.width为${read_text_src.width}")
            layoutParams.width = read_text_src.width
            read_text_line.layoutParams = layoutParams
        }
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
                read_autotitle_title_origin.visibility = View.VISIBLE
                read_autotitle_title_result.visibility = View.VISIBLE
                read_autotitle_count_origin.text = autoTitleObject.original_length.toString()
                read_autotitle_count_result.text = autoTitleObject.result_length.toString()
            }

            override fun onError(t: Throwable) {
                getErrorString(t)
            }

        })
    }

    private fun clickText() {
        val view: View =
            LayoutInflater.from(this).inflate(R.layout.layout_sheet_read, null)
        val bottomDialog = BottomSheetDialog(view.context)
        bottomDialog.setContentView(view)
        val mBehavior: BottomSheetBehavior<*> =
            BottomSheetBehavior.from(view.parent as View)
        mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        mBehavior.peekHeight = 0
        bottomDialog.show()
        view.sheet_read_font_sc.setOnClickListener {
            if (openCC != "sc") {
                val scContent =
                    ChineseConverter.convert(newObject.content, ConversionType.T2S, this)
                read_content.text = Html.fromHtml(scContent, GlideImageGetter(read_content), null)
                openCC = "sc"
            }
        }
        view.sheet_read_font_tc.setOnClickListener {
            if (openCC != "tc") {
                val tcContent =
                    ChineseConverter.convert(newObject.content, ConversionType.S2T, this)
                read_content.text = Html.fromHtml(tcContent, GlideImageGetter(read_content), null)
                openCC = "tc"
            }
        }
        view.sheet_read_text_default.setOnClickListener {
            toast(getString(R.string.read_autotitle_actionbar_text_size_default_toast))
            read_content.textSize = 18F
        }
        view.sheet_read_font_slider.addOnSliderTouchListener(object :
            Slider.OnSliderTouchListener {
            override fun onStopTrackingTouch(slider: Slider) {
                toast(getString(R.string.read_autotitle_actionbar_text_size_set) + slider.value)
                read_content.textSize = slider.value
            }

            override fun onStartTrackingTouch(slider: Slider) {}
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.read_autotitle_actionbar_text -> {
                clickText()
                true
            }
            R.id.read_autotitle_actionbar_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.read_autotitle_actionbar_share_text) +
                            newObject.title + "（" + newObject.weburl + "）"
                )
                intent.type = "text/plain"
                startActivity(intent)
                true
            }
            R.id.read_autotitle_actionbar_browser -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(newObject.weburl)))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
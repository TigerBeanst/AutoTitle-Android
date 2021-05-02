package com.jakting.autotitle.ui.my.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.jakting.autotitle.R
import com.jakting.autotitle.api.data.UserAvatarBody
import com.jakting.autotitle.api.data.UserInfoForUpdate
import com.jakting.autotitle.api.data.UserInfoUpdate
import com.jakting.autotitle.api.parse.updateUserAvatarMethod
import com.jakting.autotitle.api.parse.updateUserInfoMethod
import com.jakting.autotitle.utils.MyApplication.Companion.userInfo
import com.jakting.autotitle.utils.RetrofitCallback
import com.jakting.autotitle.utils.tools.getBytes
import com.jakting.autotitle.utils.tools.getPostBody
import com.jakting.autotitle.utils.tools.toast
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import com.lljjcoder.style.cityjd.JDCityConfig
import com.lljjcoder.style.cityjd.JDCityPicker
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.dialog_edittext.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody


class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    private val cityPicker = JDCityPicker()
    private val jdCityConfig: JDCityConfig = JDCityConfig.Builder().build()
    private val userInfoForUpdate = UserInfoForUpdate("", "", "", mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        initPicker()
        Glide.with(this@ProfileActivity)
            .load(userInfo.avatar)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(my_profile_group_1_avatar)
        my_profile_group_1_nickname.text = userInfo.nickname
        my_profile_group_1_username.text = userInfo.username
        my_profile_group_1_id.text = userInfo.id
        my_profile_group_2_city.text =
            "${userInfo.city[0]}/${userInfo.city[1]}/${userInfo.city[2]}"
        if (userInfo.sex == "male") {
            my_profile_group_2_sex_male.isChecked = true
        } else {
            my_profile_group_2_sex_female.isChecked = true
        }
        my_profile_group_1_text_title_1.setOnClickListener(this)
        my_profile_group_1_text_title_2.setOnClickListener(this)
        my_profile_group_1_text_title_3.setOnClickListener(this)
        my_profile_group_1_text_title_4.setOnClickListener(this)
        my_profile_group_2_text_title_1.setOnClickListener(this)
        my_profile_group_2_text_title_2.setOnClickListener(this)
        my_profile_group_2_text_title_3.setOnClickListener(this)
        my_profile_save.setOnClickListener(this)
    }

    private fun initPicker() {
        jdCityConfig.showType = JDCityConfig.ShowType.PRO_CITY_DIS
        cityPicker.init(this)
        cityPicker.setConfig(jdCityConfig)
        cityPicker.setOnCityItemClickListener(object : OnCityItemClickListener() {
            override fun onSelected(
                province: ProvinceBean,
                city: CityBean,
                district: DistrictBean
            ) {
                my_profile_group_2_city.text =
                    "${province.name}/${city.name}/${district.name}"
                userInfoForUpdate.city.clear()
                userInfoForUpdate.city.add(province.name)
                userInfoForUpdate.city.add(city.name)
                userInfoForUpdate.city.add(district.name)
            }

            override fun onCancel() {}
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.my_profile_group_1_text_title_1 -> {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "image/*"
                }
                startActivityForResult(intent, 42)
            }
            R.id.my_profile_group_1_text_title_2 -> {
                val dialog = MaterialAlertDialogBuilder(this)
                val viewInflated = LayoutInflater.from(this)
                    .inflate(
                        R.layout.dialog_edittext,
                        dialog_layout as ViewGroup?,
                        false
                    )
                val textInputLayout =
                    viewInflated.findViewById(R.id.dialog_textField) as TextInputLayout
                textInputLayout.hint = getString(R.string.my_profile_group_1_text_title_2)
                val editInputLayout =
                    viewInflated.findViewById(R.id.dialog_editText) as TextInputEditText
                editInputLayout.isFocusable = true
                editInputLayout.requestFocus()
                editInputLayout.setText(userInfo.nickname)
                dialog.setTitle(R.string.my_profile_group_1_text_title_2_more)
                    .setView(viewInflated)
                    .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
                    .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                        val editString = editInputLayout.text.toString()
                        if (editString.trim().isNotEmpty()) {
                            my_profile_group_1_nickname.text = editString
                            userInfoForUpdate.nickname = editString
                        } else {
                            toast(getString(R.string.no_empty))
                        }
                    }
                    .show()
            }
            R.id.my_profile_group_1_text_title_3 -> {
                val clip: ClipData = ClipData.newPlainText("", userInfo.username)
                (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
                    clip
                )
                toast(getString(R.string.my_profile_group_1_text_title_3_more))
            }
            R.id.my_profile_group_1_text_title_4 -> {
                val clip: ClipData = ClipData.newPlainText("", userInfo.id)
                (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
                    clip
                )
                toast(getString(R.string.my_profile_group_1_text_title_4_more))
            }
            R.id.my_profile_group_2_text_title_2 -> {
                cityPicker.showCityPicker()
            }
            R.id.my_profile_group_2_text_title_3 -> {
                val dialog = MaterialAlertDialogBuilder(this)
                val viewInflated = LayoutInflater.from(this)
                    .inflate(
                        R.layout.dialog_edittext,
                        dialog_layout as ViewGroup?,
                        false
                    )
                val textInputLayout =
                    viewInflated.findViewById(R.id.dialog_textField) as TextInputLayout
                textInputLayout.hint = getString(R.string.my_profile_group_2_text_title_3)
                val editInputLayout =
                    viewInflated.findViewById(R.id.dialog_editText) as TextInputEditText
                editInputLayout.isFocusable = true
                editInputLayout.requestFocus()
                editInputLayout.setText(userInfo.introduction)
                dialog.setTitle(R.string.my_profile_group_2_text_title_3_more)
                    .setView(viewInflated)
                    .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
                    .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                        val editString = editInputLayout.text.toString()
                        if (editString.trim().isNotEmpty()) {
                            userInfoForUpdate.introduction = editString
                        } else {
                            toast(getString(R.string.no_empty))
                        }
                    }
                    .show()
            }
            R.id.my_profile_save -> {
                val id: Int = my_profile_group_2_sex.checkedButtonId
                if (id == R.id.my_profile_group_2_sex_male) {
                    userInfoForUpdate.sex = "male"
                } else if (id == R.id.my_profile_group_2_sex_female) {
                    userInfoForUpdate.sex = "female"
                }
                val cityString = if (userInfoForUpdate.city.size == 0) {
                    "[]"
                } else {
                    "[\"${userInfoForUpdate.city[0]}\",\"${userInfoForUpdate.city[1]}\",\"${userInfoForUpdate.city[2]}\"]"
                }
                val createDestinationPostBody = getPostBody(
                    "{\"nickname\":\"${userInfoForUpdate.nickname}\"," +
                            "\"sex\":\"${userInfoForUpdate.sex}\"," +
                            "\"city\":$cityString," +
                            "\"introduction\":\"${userInfoForUpdate.introduction}\"}"
                )
                updateUserInfoMethod(createDestinationPostBody, object : RetrofitCallback {
                    override fun onSuccess(value: Any) {
                        val userInfoUpdate = value as UserInfoUpdate
                        var toastString = getString(R.string.my_profile_update)
                        if (userInfoUpdate.result.nickname == 1) {
                            userInfo.nickname = userInfoForUpdate.nickname
                            toastString += "【" + getString(R.string.my_profile_group_1_text_title_2) + "】"
                        }
                        if (userInfoUpdate.result.sex == 1) {
                            userInfo.sex = userInfoForUpdate.sex
                            toastString += "【" + getString(R.string.my_profile_group_2_text_title_1) + "】"
                        }
                        if (userInfoUpdate.result.city == 1) {
                            userInfo.city = userInfoForUpdate.city
                            toastString += "【" + getString(R.string.my_profile_group_2_text_title_2) + "】"
                        }
                        if (userInfoUpdate.result.introduction == 1) {
                            userInfo.introduction = userInfoForUpdate.introduction
                            toastString += "【" + getString(R.string.my_profile_group_2_text_title_3) + "】"
                        }
                        toast(toastString)
                    }

                    override fun onError(t: Throwable) {

                    }

                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 42 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data as Uri
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val requestFile: RequestBody =
                    RequestBody.create(MediaType.parse("*/*"), getBytes(inputStream))
                val body = MultipartBody.Part.createFormData("file", "image.jpg", requestFile)
                updateUserAvatarMethod(body, object : RetrofitCallback {
                    override fun onSuccess(value: Any) {
                        val userAvatarBody = value as UserAvatarBody
                        userInfo.avatar = userAvatarBody.avatar
                        Glide.with(this@ProfileActivity)
                            .load(userAvatarBody.avatar)
                            .apply(RequestOptions.bitmapTransform(CircleCrop()))
                            .into(my_profile_group_1_avatar)
                    }

                    override fun onError(t: Throwable) {
                    }

                })
            }
        }
    }
}
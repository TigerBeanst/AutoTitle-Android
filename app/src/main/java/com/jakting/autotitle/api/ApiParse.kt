package com.jakting.autotitle.api

import com.jakting.autotitle.R
import com.jakting.autotitle.api.data.*
import com.jakting.autotitle.api.parse.getAccessTokenMethod
import com.jakting.autotitle.utils.EncapsulateRetrofit
import com.jakting.autotitle.utils.MyApplication.Companion.appContext
import com.jakting.autotitle.utils.MyApplication.Companion.tokenBody
import com.jakting.autotitle.utils.RetrofitCallback
import com.jakting.autotitle.utils.tools.getErrorStatusCode
import com.jakting.autotitle.utils.tools.logd
import com.jakting.autotitle.utils.tools.longtoast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * 再封装 RxJava+Retrofit
 *
 * @param useAPI
 * @param onSuccess
 * @param onError
 */
fun accessAPI(
    useAPI: ApiParse.() -> Any,
    onSuccess: (Any) -> Unit,
    onError: (Throwable) -> Unit
) {
    val observable =
        EncapsulateRetrofit.init().useAPI() as Observable<*>
    observable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ AnyApiObject ->
            onSuccess(AnyApiObject)
        }) { t ->
            t.printStackTrace()
            try {
                if (getErrorStatusCode(t) == 401 || getErrorStatusCode(t) == 422) {
                    logd("需要刷新access_token炸了")
                    logd("=====当前refresh_token为"+tokenBody.refresh_token)
                    // access_token炸了，需要刷新
                    getAccessTokenMethod(object : RetrofitCallback {
                        override fun onSuccess(value: Any) {
                            val refreshTokenBody = value as AccessTokenBody
                            tokenBody.access_token = refreshTokenBody.access_token
                            logd("=====当前access_token为"+tokenBody.access_token)
                        }
                        override fun onError(t: Throwable) {

                        }

                    })
                }
            } catch (e: Exception) {
                longtoast(appContext.getString(R.string.server_fail))
            }

            onError(t)
        }
}

/**
 * DEMO
accessAPI(
{
getFileOrDirectoryList(getPostBody(jsonForPost))
}, { objectReturn ->
val fileOrDirectoryList = objectReturn as FileOrDirectoryList
logd("onNext // getFileOrDirectoryList")
nowFileOrDirectoryList = fileOrDirectoryList
setFileListAdapter()
}) {
logd("onError // getFileOrDirectoryList")
toast(getString(R.string.action_fail))
}
 */

interface ApiParse {

    /**
     * 传递用户名密码，获取 access_token
     * @param requestBody RequestBody
     * @return Observable<createDestination>
     */
    @POST("auth/token")
    fun getToken(@Body requestBody: RequestBody): Observable<TokenBody>

    /**
     * 传递 refresh_token，获取最新 access_token
     * @return Observable<RefreshTokenBody>
     */
    @POST("auth/refresh")
    fun getAccessToken(@Header("Authorization") refresh_token: String): Observable<AccessTokenBody>

    /**
     * 获取用户信息
     * @param access_token String
     * @return Observable<UserInfo>
     */
    @GET("user/info")
    fun getUserInfo(
        @Header("Authorization") access_token: String
    ): Observable<UserInfo>

    /**
     * 修改用户信息
     * @param access_token String
     * @param requestBody RequestBody
     * @return Observable<UserInfoUpdate>
     */
    @POST("user/info")
    fun updateUserInfo(
        @Header("Authorization") access_token: String,
        @Body requestBody: RequestBody
    ): Observable<UserInfoUpdate>

    /**
     * 上传头像
     * @param access_token String
     * @param avatar Part
     * @return Observable<UserAvatarBody>
     */
    @Multipart
    @POST("user/avatar")
    fun updateUserAvatar(
        @Header("Authorization") access_token: String,
        @Part avatar: MultipartBody.Part
    ): Observable<UserAvatarBody>

    /**
     * 获取新闻
     * @param access_token String
     * @param kind String
     * @param start String
     * @param count String
     * @return Observable<News>
     */
    @GET("data/news/{kind}/{start}/{count}")
    fun getNews(
        @Header("Authorization") access_token: String,
        @Path("kind") kind: String,
        @Path("start") start: String,
        @Path("count") count: String
    ): Observable<News>

    /**
     * 搜索新闻
     * @param access_token String
     * @param requestBody RequestBody
     * @return Observable<NewsSearch>
     */
    @POST("data/news/search")
    fun searchNews(
        @Header("Authorization") access_token: String,
        @Body requestBody: RequestBody
    ): Observable<NewsSearch>

    /**
     * 获取摘要
     * @param access_token String
     * @param requestBody RequestBody
     * @return Observable<AutoTitleObject>
     */
    @POST("data/autotitle")
    fun getAutoTitle(
        @Header("Authorization") access_token: String,
        @Body requestBody: RequestBody
    ): Observable<AutoTitleObject>

}
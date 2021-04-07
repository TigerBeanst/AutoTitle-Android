package com.jakting.rn6pan.api

import com.jakting.autotitle.api.data.News
import com.jakting.autotitle.api.data.TokenBody
import com.jakting.autotitle.api.data.UserInfo
import com.jakting.autotitle.utils.EncapsulateRetrofit
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
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

    /*
    登录请求
     */

    /**
     * 传递用户名密码，获取 access_token
     * @param requestBody RequestBody
     * @return Observable<createDestination>
     */
    @POST("auth/token")
    fun getToken(@Body requestBody: RequestBody): Observable<TokenBody>

    /*
    用户信息
     */

    /**
     * 获取用户信息
     * @param requestBody RequestBody
     * @return Observable<UserInfo>
     */
    @GET("user/info")
    fun getUserInfo(
        @Header("Authorization") access_token: String
    ): Observable<UserInfo>


    /*
    用户信息
     */

    /**
     * 获取用户信息
     * @param requestBody RequestBody
     * @return Observable<UserInfo>
     */
    @GET("user/info")
    fun getNews(
        @Header("Authorization") access_token: String,
        @Body requestBody: RequestBody
    ): Observable<News>

}
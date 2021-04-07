package com.jakting.autotitle.utils

import com.github.simonpercic.oklog3.OkLogInterceptor
import com.jakting.autotitle.BuildConfig
import com.jakting.rn6pan.api.ApiParse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class EncapsulateRetrofit {
    companion object {
        fun init(): ApiParse {
            val okHttpBuilder = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val okLogInterceptor = OkLogInterceptor.builder().build()
                okHttpBuilder.addInterceptor(okLogInterceptor)
            }
            val okHttpClient = okHttpBuilder
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiParse::class.java)
        }
    }
}
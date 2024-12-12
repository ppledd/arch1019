package com.zjy.architecture.ext

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

/**
 * @author zhengjy
 * @since 2019/11/04
 * Description:创建[OkHttpClient]和[Retrofit]的方法
 */

fun OkHttpClient.Builder.addInterceptors(vararg interceptor: Interceptor): OkHttpClient.Builder {
    interceptor.forEach {
        addInterceptor(it)
    }
    return this
}

fun OkHttpClient.Builder.addSSLSocketFactory(sslFactory: SSLSocketFactory?, trustManager: X509TrustManager?): OkHttpClient.Builder {
    if (sslFactory != null && trustManager != null) {
        sslSocketFactory(sslFactory, trustManager)
    }
    return this
}

/**
 * 创建Retrofit对象通用方法
 *
 * @param client    实际请求的OkHttpClient
 * @param gson      Gson
 * @param url       请求接口的baseUrl
 */
fun createRetrofit(client: OkHttpClient, gson: Gson, url: String): Retrofit {
    return Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(url)
        .build()
}

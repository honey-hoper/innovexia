package com.webhopers.innovexia.services

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class WooCommerceClient {

    companion object {

        private val BASE_URL = "http://www.innovexia.com/wp-json/wc/v2/"
        private val BASE_URL_LOGIN = "http://www.innovexia.com/wp-json/"
        private val KEY = "ck_67ce36b504c739ab8923eccc1776aa3a38eb66c1"
        private val SECRET = "cs_3692af67a0cf8f8d3f7843200e698898778d4427"

        fun get(): Retrofit {
            val oauthInterceptor = OAuthInterceptor(KEY, SECRET)
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .addInterceptor(oauthInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .hostnameVerifier { hostname, session ->  return@hostnameVerifier true}
                    .build()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
        }

        fun getSimple(): Retrofit {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .build()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL_LOGIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
        }
    }


}
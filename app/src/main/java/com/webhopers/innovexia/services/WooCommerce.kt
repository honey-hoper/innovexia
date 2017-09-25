package com.webhopers.innovexia.services

import com.webhopers.innovexia.models.ProductCategory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WooCommerce {

    @GET("products/categories")
    fun categories(@Query("per_page") perPage: String = "100", @Query("order") order: String = "asc"): Call<List<ProductCategory>>
}
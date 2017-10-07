package com.webhopers.innovexia.services

import com.webhopers.innovexia.models.*
import retrofit2.Call
import retrofit2.http.*

interface WooCommerce {

    @GET("products/categories")
    fun categories(@Query("per_page") perPage: String = "100", @Query("order") order: String = "asc"): Call<List<ProductCategory>>

    @GET("products")
    fun productsByCategories(@Query("category") category: String, @Query("per_page") perPage: String = "100", @Query("order") order: String = "asc", @Query("orderby") orderBy: String = "title"): Call<List<Product>>

    @POST("customer/login")
    fun loginCustomer(@Body customerCredentials: CustomerCredentials): Call<CustomerLoginResponse>

    @GET("customers/{id}")
    fun getCustomer(@Path("id") id: Long): Call<Customer>

    @POST("customers")
    fun createCustomer(@Body customer: Customer): Call<Customer>

}


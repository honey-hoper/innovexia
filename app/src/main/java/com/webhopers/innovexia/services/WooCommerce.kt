package com.webhopers.innovexia.services

import com.webhopers.innovexia.models.*
import retrofit2.Call
import retrofit2.http.*
import java.util.function.LongToDoubleFunction

interface WooCommerce {

    @GET("products/categories")
    fun categories(@Query("per_page") perPage: String = "100", @Query("order") order: String = "asc"): Call<List<ProductCategory>>

    @GET("products")
    fun products(@Query("per_page") perPage: String = "100", @Query("offset") offset: String = "0", @Query("order") order: String = "asc", @Query("orderby") orderBy: String = "title"): Call<List<Product>>

    @GET("products")
    fun productsByCategories(@Query("category") category: String, @Query("per_page") perPage: String = "100", @Query("order") order: String = "asc", @Query("orderby") orderBy: String = "title"): Call<List<Product>>

    @POST("customer/login")
    fun loginCustomer(@Body customerCredentials: CustomerCredentials): Call<CustomerLoginResponse>

    @GET("customers/{id}")
    fun getCustomer(@Path("id") id: Long): Call<Customer>

    @POST("customers")
    fun createCustomer(@Body customer: Customer): Call<Customer>

    @POST("customers/{id}")
    fun updateCustomer(@Path("id") id: String, @Body customer: Customer): Call<Customer>

    @GET("buyer/list")
    fun getBuyerList(): Call<List<Buyer>>

    @POST("visit/add")
    fun addVisit(@Body visit: Visit): Call<Any>

    @GET("visit/list")
    fun getCustomerVisits(@Query("id") id: Long): Call<List<Visit>>


}


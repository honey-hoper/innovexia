package com.webhopers.innovexia.models

import com.google.gson.annotations.SerializedName

class Customer (
        @SerializedName("id")
        var id: Long? = null,
        @SerializedName("email")
        var email: String? = null,
        @SerializedName("first_name")
        var firstName: String? = null,
        @SerializedName("last_name")
        var lastName: String? = null,
        @SerializedName("username")
        var username: String? = null
)


/**
 *
 * customer login post data
 */
class CustomerCredentials (
        @SerializedName("user_login")
        val email: String,
        @SerializedName("user_password")
        val password: String
)

/**
 *
 * models for customer login response
 * CustomerLoginResponse
 * Data
 */
class CustomerLoginResponse (
        @SerializedName("data")
        var data: Data? = null
)

class Data (
        @SerializedName("ID")
        var id: Long? = null
)
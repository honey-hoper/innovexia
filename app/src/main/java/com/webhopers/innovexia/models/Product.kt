package com.webhopers.innovexia.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Product (
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("publish_in_app")
        var publish: Boolean? = true,
        @SerializedName("product_visualate")
        var label: String? = null
) : Serializable


class ProductCategory (
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("publish_in_app")
        var publish: Boolean? = true

) : Serializable
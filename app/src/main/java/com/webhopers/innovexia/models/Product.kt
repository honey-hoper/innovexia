package com.webhopers.innovexia.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Product (
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("images")
        var images: List<Image>? = null
) : Serializable

class Image (
        @SerializedName("src")
        var src: String? = null
) : Serializable

class ProductCategory (
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("publish_in_app")
        var publish: Boolean = true

) : Serializable
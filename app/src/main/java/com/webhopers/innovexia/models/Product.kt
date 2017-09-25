package com.webhopers.innovexia.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ProductCategory (
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("name")
        var name: String? = null
) : Serializable
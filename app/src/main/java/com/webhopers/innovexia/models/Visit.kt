package com.webhopers.innovexia.models

import com.google.gson.annotations.SerializedName
import java.util.*

class Visit (
        @SerializedName("mr_id")
        var customerId: Int? = null,
        @SerializedName("buyer_id")
        var buyerId: Int? = null,
        @SerializedName("created_at")
        var dateTime: String? = null,
        @SerializedName("location")
        var location: String? = null,
        @SerializedName("product_id")
        var productIds: String? = null
)
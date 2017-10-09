package com.webhopers.innovexia.models

import java.io.Serializable

class Buyer(
        var id: Long? = null,
        var name: String? = null,
        var type: String? = null,
        var address: String? = null
) : Serializable
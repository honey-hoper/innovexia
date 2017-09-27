package com.webhopers.innovexia.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Slide(
        @PrimaryKey
        var name: String? = null,
        var urls: RealmList<ImageUrl>? = null
) : RealmObject()

open class ImageUrl(
        var url: String? = null
) : RealmObject()
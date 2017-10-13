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


open class CustomerRealm(
        @PrimaryKey
        var id: Long? = null,
        var email: String? = null,
        var firstName: String? = null,
        var lastName: String? = null,
        var username: String? = null
) : RealmObject()

open class ProductRealm(
        @PrimaryKey
        var id: String? = null,
        var name: String? = null,
        var label: String? = null,
        var publish: Boolean? = null
) : RealmObject()

open class ProductCategoryRealm(
        @PrimaryKey
        var id: String? = null,
        var name: String? = null,
        var publish: Boolean? = null
) : RealmObject()
package com.webhopers.innovexia.utils

import android.support.design.widget.TextInputEditText
import android.view.View
import com.webhopers.innovexia.models.*

fun View.show(bool: Boolean) {
    if (bool) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

fun TextInputEditText.value() = this.text.toString().trim()

fun Customer.convertToCustomerRealm(): CustomerRealm {

    val phone = this.metaData?.findLast { it.key == "phone1" }

    return CustomerRealm(
            this.id,
            this.email,
            this.firstName,
            this.lastName,
            this.username,
            phone?.value
    )
}

fun CustomerRealm.convertToCustomer(): Customer {
    return Customer(
            this.id,
            this.email,
            this.firstName,
            this.lastName,
            this.username,
            metaData = listOf<MetaData>(MetaData("phone1", this.phone))
    )
}

fun Product.convertToProductRealm() : ProductRealm {
    return ProductRealm(
            this.id,
            this.name,
            this.label,
            this.publish
    )
}

fun ProductRealm.convertToProduct() : Product {
    return Product(
            this.id,
            this.name,
            this.publish,
            this.label
    )

}

fun ProductCategory.convertToProductCategoryRealm() : ProductCategoryRealm {
    return ProductCategoryRealm(
            this.id,
            this.name,
            this.publish
    )
}

fun ProductCategoryRealm.convertToProductCategory() : ProductCategory {
    return ProductCategory(
            this.id,
            this.name,
            this.publish
    )
}



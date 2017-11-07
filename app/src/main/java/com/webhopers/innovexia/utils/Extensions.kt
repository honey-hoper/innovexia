package com.webhopers.innovexia.utils

import android.support.design.widget.TextInputEditText
import android.view.View
import com.webhopers.innovexia.models.*
import java.text.SimpleDateFormat
import java.util.*

fun View.show(bool: Boolean) {
    if (bool) this.visibility = View.VISIBLE
    else this.visibility = View.INVISIBLE
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

fun Buyer.convertToBuyerRealm() : BuyerRealm {
    return BuyerRealm(
            this.id,
            this.name,
            this.address,
            this.type,
            this.active
    )
}

fun BuyerRealm.convertToBuyer() : Buyer {
    return Buyer(
            this.id,
            this.name,
            this.address,
            this.type,
            this.active
    )
}

fun String.getDate(): String {
    val dateTokens = this.split(" ")[0].split("-")
    return "${dateTokens[2]}/${dateTokens[1]}/${dateTokens[0]}"
}

fun String.getTime(): String {
    val timeTokens = this.split(" ")[1].split(":")
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR, timeTokens[0].toInt())
    calendar.set(Calendar.MINUTE, timeTokens[1].toInt())

    val timeFormatter = SimpleDateFormat("hh:mm a")
    return timeFormatter.format(calendar.time)
}


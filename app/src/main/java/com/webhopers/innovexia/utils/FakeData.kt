package com.webhopers.innovexia.utils

import com.webhopers.innovexia.models.Buyer
import com.webhopers.innovexia.models.Product


object FakeData {
    fun getBuyers(): List<Buyer> {
        return listOf(
                Buyer(name = "Honey", type = 0, address = "Patiala"),
                Buyer(name = "Honda", type = 0, address = "Royal Estate, Zirkpur"),
                Buyer(name = "Homey Bro", type = 0, address = "Deelwal, Patiala"),
                Buyer(name = "Mark Zuckerberg", type = 0, address = "FB Headquarters, U.S."),
                Buyer(name = "Harsh", type = 0, address = "Ludhiana"),
                Buyer(name = "Hooman", type = 0, address = "Bypass, Faridabad"),
                Buyer(name = "Hoo", type = 0, address = "Poland, Holand"),
                Buyer(name = "Honey", type = 0, address = "Barelli, Chandigarh"),
                Buyer(name = "Hero", type = 0, address = "Mumbai, Maharashtra"),
                Buyer(name = "Akon", type = 0, address = "Africa"),
                Buyer(name = "Usain Bolt", type = 0, address = "Forest, Jungles")
                )
    }

    fun getProducts(): List<Product> {
        return listOf(
                Product(id = "1", name = "Acesoft"),
                Product(id = "2", name = "Nicip MR"),
                Product(id = "3", name = "Paracetamol"),
                Product(id = "4", name = "Disprin"),
                Product(id = "5", name = "Vicks"),
                Product(id = "6", name = "Kuka"),
                Product(id = "7", name = "Benadryl"),
                Product(id = "8", name = "Volini"),
                Product(id = "9", name = "moov"),
                Product(id = "10", name = "Itch guard"),
                Product(id = "11", name = "Tablet general")
        )
    }
}
package com.webhopers.innovexia.utils

import com.webhopers.innovexia.models.Buyer


object FakeData {
    fun getBuyers(): List<Buyer> {
        return listOf(
                Buyer(name = "Honey", type = "Doctor", address = "Patiala"),
                Buyer(name = "Honda", type = "Doctor", address = "Royal Estate, Zirkpur"),
                Buyer(name = "Homey Bro", type = "Chemist", address = "Deelwal, Patiala"),
                Buyer(name = "Mark Zuckerberg", type = "Doctor", address = "FB Headquarters, U.S."),
                Buyer(name = "Harsh", type = "Chemist", address = "Ludhiana"),
                Buyer(name = "Hooman", type = "Doctor", address = "Bypass, Faridabad"),
                Buyer(name = "Hoo", type = "Chemist", address = "Poland, Holand"),
                Buyer(name = "Honey", type = "Chemist", address = "Barelli, Chandigarh"),
                Buyer(name = "Hero", type = "Doctor", address = "Mumbai, Maharashtra"),
                Buyer(name = "Akon", type = "Singer", address = "Africa"),
                Buyer(name = "Usain Bolt", type = "Doctor", address = "Forest, Jungles")
                )
    }
}
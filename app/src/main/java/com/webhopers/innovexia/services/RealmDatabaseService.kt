package com.webhopers.innovexia.services

import com.webhopers.innovexia.models.CustomerRealm
import com.webhopers.innovexia.models.ImageUrl
import com.webhopers.innovexia.models.Slide
import io.realm.Realm


class RealmDatabaseService {

    companion object {
        val realm by lazy { Realm.getDefaultInstance() }

        fun createSlide(slide: Slide) {
            realm.executeTransaction {
                it.copyToRealm(slide)
            }
        }

        fun getSlides(): List<Slide> {
            val result = realm.where(Slide::class.java)
                    .findAll()
            return result
        }

        fun getSlide(slideName: String): Slide? {
            val result = realm.where(Slide::class.java)
                    .equalTo("name", slideName)
                    .findFirst()
            return result
        }

        fun addToSlide(slideName: String, urls: List<ImageUrl>) {
            val result = realm.where(Slide::class.java)
                    .equalTo("name", slideName)
                    .findAll()

            realm.executeTransaction {
                result[0].urls?.addAll(urls)
            }
        }

        fun removeSlide(slideName: String) {
            val result = realm.where(Slide::class.java)
                    .equalTo("name", slideName)
                    .findFirst()

            realm.executeTransaction {
                result?.deleteFromRealm()
            }
        }

        fun saveCustomer(customer: CustomerRealm) {
            realm.executeTransaction {
                it.insert(customer)
            }
        }

        fun getCustomer() : CustomerRealm {
            return realm.where(CustomerRealm::class.java).findFirst()!!
        }

        fun removeCustomer() {
            val result = realm.where(CustomerRealm::class.java).findAll()
            realm.executeTransaction {
                result.forEach { it.deleteFromRealm() }
            }
        }

    }
}
package com.webhopers.innovexia.services

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

    }
}
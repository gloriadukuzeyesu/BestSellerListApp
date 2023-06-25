package com.codepath.bestsellerlistapp

import com.google.gson.annotations.SerializedName

/**
 * The Model for storing a single book from the NY Times API
 *
 * SerializedName tags MUST match the JSON response for the
 * object to correctly parse with the gson library.
 */
class BestSellerBook {
    @SerializedName("rank")
    var rank = 0
//    @JvmField
    @SerializedName("title")
    var title: String? = null


    @SerializedName("author")
    var author: String? = null


    @SerializedName("book_image")
    var bookImageUrl : String? = null


    @SerializedName("description") // used by the gson library to select which JSON key to import data from.
    var description : String? = null


    @SerializedName("amazon_product_url")
    var amazonUrl : String? = null
}
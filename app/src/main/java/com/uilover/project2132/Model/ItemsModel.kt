package com.uilover.project2132.Model

import java.io.Serializable

data class ProductGallery(
    val img1: String = "",
    val img2: String = "",
    val img3: String = ""
) : Serializable

data class ItemsModel(
    var title: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var image: String = "",  // Main product image
    var product_gallery: ProductGallery = ProductGallery(),
    var categoryId: String = "",
    var categoryTitle: String = "",
    var showRecommend: Boolean = false,
    var rated: Double = 0.0,
    var numberInCart: Int = 0,  // For cart management (not in Firebase)
    var selectedSize: String = "",  // Selected size for cart
    var selectedColor: String = ""  // Selected color for cart
) : Serializable

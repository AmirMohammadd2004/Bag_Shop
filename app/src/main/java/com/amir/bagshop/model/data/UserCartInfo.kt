package com.amir.bagshop.model.data

import com.amir.bagshop.model.data.ProductsResponse.Product


data class UserCartInfo(
    val success: Boolean,
    val productList: List<Product>,
    val message: String,
    val totalPrice: Int
)
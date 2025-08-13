package com.amir.bagshop.model.repository.product

import com.amir.bagshop.model.data.AdsResponse
import com.amir.bagshop.model.data.ProductsResponse
import com.amir.bagshop.model.data.ProductsResponse.Product


interface ProductRepository {


    suspend fun getAllProducts( isInternetConnected : Boolean): List<ProductsResponse.Product>

    suspend fun getAllAds( isInternetConnected : Boolean): List<AdsResponse.Ad>

    suspend fun getAllCategory (categoryId: String): List<ProductsResponse.Product>


    suspend fun getById(idProduct: String):Product
}
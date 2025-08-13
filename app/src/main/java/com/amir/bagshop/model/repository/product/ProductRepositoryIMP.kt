package com.amir.bagshop.model.repository.product

import com.amir.bagshop.model.data.AdsResponse
import com.amir.bagshop.model.data.ProductsResponse
import com.amir.bagshop.model.db.ProductDao
import com.amir.bagshop.model.net.ApiService

class ProductRepositoryIMP(

    val apiService: ApiService,
    val productDao: ProductDao

) : ProductRepository {

    override suspend fun getAllProducts(isInternetConnected: Boolean): List<ProductsResponse.Product> {

        if (isInternetConnected) {
            //Get Data From net

            val dataFromServer = apiService.getAllProducts()
            if (dataFromServer.success) {
                productDao.insertOrUpdate(dataFromServer.products)
                return dataFromServer.products
            }
        } else {
            //gat data from local
            return productDao.getAll()

        }
        return listOf()
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun getAllAds(isInternetConnected: Boolean): List<AdsResponse.Ad> {

        if (isInternetConnected) {
            val dataFromServer = apiService.getAllAds()
            if (dataFromServer.success) {

                return dataFromServer.ads
            }
        }

        return listOf()
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override suspend fun getAllCategory(categoryId: String): List<ProductsResponse.Product> {

        return productDao.getCategoryById(categoryId)
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override suspend fun getById(idProduct: String): ProductsResponse.Product {
        return productDao.getById(idProduct)
    }


}
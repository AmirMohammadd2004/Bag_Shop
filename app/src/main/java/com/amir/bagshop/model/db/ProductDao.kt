package com.amir.bagshop.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amir.bagshop.model.data.ProductsResponse

@Dao
interface ProductDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(products: List<ProductsResponse.Product>)

    @Query("SELECT * FROM product_table")
    suspend fun getAll(): List<ProductsResponse.Product>

    @Query("SELECT * FROM product_table WHERE productId = :productId")
    suspend fun getById(productId: String): ProductsResponse.Product


    @Query("SELECT * FROM product_table WHERE category = :categoryId")
    suspend fun getCategoryById(categoryId: String): List<ProductsResponse.Product>

}
package com.amir.bagshop.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amir.bagshop.model.data.ProductsResponse


@Database(entities = [ProductsResponse.Product::class] , version = 1 , exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao() : ProductDao

}
package com.amir.bagshop.model.repository.cart

import com.amir.bagshop.model.data.CheckOut
import com.amir.bagshop.model.data.SubmitOrder
import com.amir.bagshop.model.data.UserCartInfo

interface AddToCartRepository {

    suspend fun addedToCart(productId: String): Boolean

    suspend fun removeFromCart(productId: String) : Boolean

    suspend fun getCartSize(): Int

    suspend fun getUserCartInfo(): UserCartInfo



    suspend fun submitOrder(address : String , postalCode: String): SubmitOrder
    suspend fun checkout(orderId: String): CheckOut


     fun setOrderId(orderId: String)
     fun getOrderId(): String


     fun setPurchaseStatus(status: Int)
     fun getPurchaseStatus(): Int





}
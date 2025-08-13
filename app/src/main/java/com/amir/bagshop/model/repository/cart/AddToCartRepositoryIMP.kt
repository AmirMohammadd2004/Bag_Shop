package com.amir.bagshop.model.repository.cart

import android.content.SharedPreferences
import androidx.core.content.edit
import com.amir.bagshop.model.data.CheckOut
import com.amir.bagshop.model.data.SubmitOrder
import com.amir.bagshop.model.data.UserCartInfo
import com.amir.bagshop.model.net.ApiService
import com.amir.bagshop.util.NO_PAYMENT
import com.google.gson.JsonObject


class AddToCartRepositoryIMP(
    private val apiService: ApiService,
    private val shared: SharedPreferences
) : AddToCartRepository {

    override suspend fun addedToCart(productId: String): Boolean {

        val jsonObject = JsonObject().apply {

            addProperty("productId", productId)
        }
        return apiService.addProductToCart(jsonObject).success

    }


    override suspend fun removeFromCart(productId: String): Boolean {

        val jsonObject = JsonObject().apply {

            addProperty("productId", productId)
        }
        return apiService.removeFromCart(jsonObject).success
    }


    override suspend fun getCartSize(): Int {

        val result = apiService.getUserCart()

        if (result.success) {
            var counter = 0
            result.productList.forEach {
                counter += (it.quantity ?: "0").toInt()
            }
            return counter
        }
        return 0
    }


    override suspend fun getUserCartInfo(): UserCartInfo {

        return apiService.getUserCart()
    }


    override suspend fun submitOrder(
        address: String,
        postalCode: String
    ): SubmitOrder {

        val jsonObject = JsonObject().apply {

            addProperty("address" ,address)
            addProperty("postalCode" ,postalCode)
        }
        val result = apiService.submitOrder(jsonObject)
        setOrderId(result.orderId.toString())
        return  result
    }





    override suspend fun checkout(orderId: String): CheckOut {


        val jsonObject= JsonObject().apply {

            addProperty("orderId",orderId)
        }

        return apiService.checkout(jsonObject)

    }





    override  fun setOrderId(orderId: String) {

        shared.edit { putString("OrderId", orderId) }
    }
    override  fun getOrderId(): String {

        return shared.getString("OrderId","0") !!
    }






    override  fun setPurchaseStatus(status: Int) {

        shared.edit { putInt("Purchase_Status", status) }
    }
    override  fun getPurchaseStatus(): Int {

        return shared.getInt("Purchase_Status", NO_PAYMENT)
    }
}


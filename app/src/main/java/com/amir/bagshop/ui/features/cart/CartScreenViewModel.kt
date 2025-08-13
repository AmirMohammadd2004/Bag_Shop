package com.amir.bagshop.ui.features.cart

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amir.bagshop.model.data.ProductsResponse
import com.amir.bagshop.model.repository.cart.AddToCartRepository
import com.amir.bagshop.model.repository.user.UserRepository
import com.amir.bagshop.util.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartScreenViewModel(

    private val addToCartRepository: AddToCartRepository,
    private val userRepository: UserRepository

) : ViewModel() {


    val totalPrice = mutableIntStateOf(0)
    val productList = mutableStateOf(listOf<ProductsResponse.Product>())
    val isChangingNumber = mutableStateOf(Pair("", false))


    fun setPaymentStatus(status: Int) {

        addToCartRepository.setPurchaseStatus(status)

    }


    fun getUserLocation(): Pair<String, String> {

        return userRepository.getUserLocation()
    }


    fun setUserLocation(address: String, postalCode: String) {

        userRepository.saveUserLocation(address, postalCode)

    }


    fun purchaseAll(address: String, postalCode: String, isSuccess: (Boolean, String) -> Unit) {

        viewModelScope.launch(coroutineExceptionHandler) {

            val result = addToCartRepository.submitOrder(address, postalCode)

            isSuccess.invoke(result.success, result.paymentLink)

        }


    }


    fun loadCartData() {

        viewModelScope.launch(coroutineExceptionHandler) {

            val data = addToCartRepository.getUserCartInfo()

            totalPrice.intValue = data.totalPrice
            productList.value = data.productList

        }
    }

    fun addItem(productId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {

            isChangingNumber.value = isChangingNumber.value.copy(productId, true)
            val success = addToCartRepository.addedToCart(productId)
            if (success) {
                loadCartData()
            }
            delay(500)
            isChangingNumber.value = isChangingNumber.value.copy(productId, false)


        }

    }

    fun removeItem(productId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {


            isChangingNumber.value = isChangingNumber.value.copy(productId, true)
            val success = addToCartRepository.removeFromCart(productId)
            if (success) {
                loadCartData()
            }
            delay(500)
            isChangingNumber.value = isChangingNumber.value.copy(productId, false)
        }
    }
}
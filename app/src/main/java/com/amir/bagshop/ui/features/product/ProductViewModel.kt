package com.amir.bagshop.ui.features.product

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amir.bagshop.model.data.Comment
import com.amir.bagshop.model.data.ProductsResponse
import com.amir.bagshop.model.data.ProductsResponse.Product
import com.amir.bagshop.model.repository.cart.AddToCartRepository
import com.amir.bagshop.model.repository.comments.CommentRepository
import com.amir.bagshop.model.repository.product.ProductRepository
import com.amir.bagshop.util.EMPTY_PRODUCT
import com.amir.bagshop.util.coroutineExceptionHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class ProductViewModel(
    private val productRepository: ProductRepository,
    private val commentRepository: CommentRepository,
    private val addToCartRepository: AddToCartRepository

) : ViewModel() {

    val dataProductById = mutableStateOf<Product>(EMPTY_PRODUCT)
    val comments = mutableStateOf<List<Comment>>(listOf())
    val showAnimation = mutableStateOf(false)
    val badgeNumber = mutableIntStateOf(0)


    fun loadData(dataById: String, isInternetConnected: Boolean) {
        getDataById(dataById)
        if (isInternetConnected) {
            loadAllComments(dataById)
            badgeNumber()
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private fun getDataById(dataById: String) {

        viewModelScope.launch(coroutineExceptionHandler) {

            dataProductById.value = productRepository.getById(dataById)
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private fun loadAllComments(productId: String) {

        viewModelScope.launch(coroutineExceptionHandler) {

            comments.value = commentRepository.getAllComments(productId)
        }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    fun addNewComment(productId: String, text: String, isSuccess: (String) -> Unit) {

        viewModelScope.launch(coroutineExceptionHandler) {

            commentRepository.pushComment(productId, text, isSuccess)

            delay(2000)

            comments.value = commentRepository.getAllComments(productId)

        }


    }

    private fun badgeNumber() {

        viewModelScope.launch(coroutineExceptionHandler) {

            badgeNumber.intValue = addToCartRepository.getCartSize()

        }

    }


    fun addProductToCart(productId: String, addToCartMessage: (String) -> Unit) {

        viewModelScope.launch(coroutineExceptionHandler) {

            showAnimation.value = true

            val result = addToCartRepository.addedToCart(productId)

            if (result) {
                addToCartMessage.invoke("Added To Cart Successful")
            } else {
                addToCartMessage.invoke("Added TO Cart UnSuccessful")
            }
            delay(1000)
            showAnimation.value = false

        }

    }


}
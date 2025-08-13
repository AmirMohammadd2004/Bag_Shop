package com.amir.bagshop.ui.features.Main

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amir.bagshop.model.data.AdsResponse
import com.amir.bagshop.model.data.CheckOut
import com.amir.bagshop.model.data.ProductsResponse
import com.amir.bagshop.model.repository.cart.AddToCartRepository
import com.amir.bagshop.model.repository.product.ProductRepository
import com.amir.bagshop.util.coroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainViewmodel(
    private val productRepository: ProductRepository,
    private val addToCartRepository: AddToCartRepository,
    isInternetConnected: Boolean
) : ViewModel() {

    val showProgressBar = mutableStateOf(true)

    val showPaymentDialog = mutableStateOf(false)
    val checkOutData = mutableStateOf(CheckOut(null , null))

    init {

        refreshAllDataFromNet(isInternetConnected)
    }


    fun getCheckoutData(){

        viewModelScope.launch (coroutineExceptionHandler){

           val result= addToCartRepository.checkout(addToCartRepository.getOrderId())
            if (result.success!!){
                checkOutData.value = result
                showPaymentDialog.value = true
            }

        }
    }


    fun getPaymentStatus(): Int {

        return addToCartRepository.getPurchaseStatus()
    }


    fun setPaymentStatus(status: Int) {

        addToCartRepository.setPurchaseStatus(status)
    }

    val dataProduct = mutableStateOf<List<ProductsResponse.Product>>(listOf())
    val dataAds = mutableStateOf<List<AdsResponse.Ad>>(listOf())
    val badgeNumber = mutableIntStateOf(0)


    private fun refreshAllDataFromNet(isInternetConnected: Boolean) {

        viewModelScope.launch(coroutineExceptionHandler) {
            if (isInternetConnected) {

                delay(1200)

                val newDataProduct = async { productRepository.getAllProducts(isInternetConnected) }
                val newDataAds = async { productRepository.getAllAds(isInternetConnected) }

                updateData(newDataProduct.await(), newDataAds.await())

                showProgressBar.value = false

            }
        }
    }


    private fun updateData(product: List<ProductsResponse.Product>, ads: List<AdsResponse.Ad>) {
        dataProduct.value = product
        dataAds.value = ads
    }


    fun badgeNumber() {

        viewModelScope.launch(coroutineExceptionHandler) {

            badgeNumber.intValue = addToCartRepository.getCartSize()
        }
    }

}


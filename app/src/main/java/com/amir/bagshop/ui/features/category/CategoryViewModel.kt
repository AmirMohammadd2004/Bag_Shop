package com.amir.bagshop.ui.features.category

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amir.bagshop.model.data.ProductsResponse
import com.amir.bagshop.model.repository.product.ProductRepository
import com.amir.bagshop.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class CategoryViewModel(

    private val productRepository: ProductRepository

) : ViewModel() {


    val dataFromLocal = mutableStateOf<List<ProductsResponse.Product>>( listOf() )

    fun getDataFromLocal( categoryId: String ) {


        viewModelScope.launch (coroutineExceptionHandler){

          val data =  productRepository.getAllCategory(categoryId)
            dataFromLocal.value = data
        }


    }
}
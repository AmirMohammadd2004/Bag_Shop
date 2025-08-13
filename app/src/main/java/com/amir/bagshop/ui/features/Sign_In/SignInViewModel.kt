package com.amir.bagshop.ui.features.Sign_In

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amir.bagshop.model.repository.user.UserRepository
import com.amir.bagshop.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class SignInViewModel(private val userRepository: UserRepository) : ViewModel() {


    val email = MutableLiveData("")
    val password = MutableLiveData("")

    fun stateSignIn( LoginEvent: (String) -> Unit) {

        viewModelScope.launch(coroutineExceptionHandler) {

            val result = userRepository.signIn(email.value!!, password.value!!)

            LoginEvent(result)

        }

    }

}
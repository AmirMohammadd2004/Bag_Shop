package com.amir.bagshop.ui.features.sign_Up

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amir.bagshop.model.repository.user.UserRepository
import com.amir.bagshop.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {


    val name = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val confirmPassword = MutableLiveData("")

    fun stateSignUp( LogginEvent:( String )-> Unit ) {

        viewModelScope.launch(coroutineExceptionHandler) {

            val result = userRepository.signUp(name.value!! ,email.value!! , password.value!!)

            LogginEvent( result )

        }
    }
}
package com.amir.bagshop.ui.features.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.amir.bagshop.model.repository.user.UserRepository

class ProfileViewModel(

    private val userRepository: UserRepository

): ViewModel() {

    val email = mutableStateOf("")
    val address = mutableStateOf("")
    val postalCode = mutableStateOf("")
    val loginTime = mutableStateOf("")
    val showDialog = mutableStateOf(false)


    fun loadUserData(){

        email.value = userRepository.getUserName()!!
        loginTime.value = userRepository.getUserLoginTime()

        val location = userRepository.getUserLocation()
        address.value = location.first
        postalCode.value =location.second

    }

    fun signOut(){
        userRepository.signOut()
    }


    fun setUserLocation(address: String , postalCode: String){


        userRepository.saveUserLocation( address ,postalCode)
    }


}
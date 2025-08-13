package com.amir.bagshop.model.repository.user

interface UserRepository {


    // online
    suspend fun signUp(name: String, username: String, password: String): String

    suspend fun signIn(username: String, password: String): String


    // offline
    fun signOut()


    fun loadToken()
    fun saveToken(newToken: String)
    fun getToken(): String?


    // for in app
    fun saveUserName(username: String)
    fun getUserName(): String?


    //time
    fun saveUSerLoginTime()
    fun getUserLoginTime(): String


    //address
    fun saveUserLocation( address: String, postalCode: String)
    fun getUserLocation(): Pair<String, String>


}
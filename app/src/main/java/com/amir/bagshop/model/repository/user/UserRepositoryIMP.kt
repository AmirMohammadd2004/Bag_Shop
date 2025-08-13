package com.amir.bagshop.model.repository.user

import android.content.SharedPreferences
import androidx.compose.ui.platform.LocalContext
import com.amir.bagshop.model.net.ApiService
import com.amir.bagshop.util.VALUE_SUCCESS
import com.google.gson.JsonObject
import androidx.core.content.edit
import com.amir.bagshop.model.repository.TokenInMemory

class UserRepositoryIMP(
    private val apiService: ApiService,
    private val shared: SharedPreferences
) : UserRepository {


    override suspend fun signUp(name: String, username: String, password: String): String {

        val jsonObject = JsonObject().apply {
            addProperty("name", name)
            addProperty("email", username)
            addProperty("password", password)
        }


        val result = apiService.signUp(jsonObject)

        if (result.success) {

            TokenInMemory.refreshToken(result.token, username)
            saveToken(result.token)
            saveUserName(username)
            saveUSerLoginTime()


            return VALUE_SUCCESS

        } else {

            return result.message


        }
    }


    override suspend fun signIn(username: String, password: String): String {


        val jsonObject = JsonObject().apply {

            addProperty("email", username)
            addProperty("password", password)
        }


        val result = apiService.signIn(jsonObject)

        if (result.success) {
            TokenInMemory.refreshToken(result.token, username)
            saveToken(result.token)
            saveUserName(username)
            saveUSerLoginTime()
            return VALUE_SUCCESS


        } else {
            return result.message
        }

    }






    override fun saveToken(newToken: String) {

        shared.edit { putString("token", newToken) }
    }
    override fun getToken(): String? {
        return shared.getString("token", null)
    }
    override fun loadToken() {

        TokenInMemory.refreshToken(getToken(), getUserName())
    }


    override fun saveUserName(username: String) {

        shared.edit { putString("username", username) }
    }
    override fun getUserName(): String? {
        return shared.getString("username", null)
    }




    ///////time
    override fun saveUSerLoginTime() {
        val now = System.currentTimeMillis()
        shared.edit { putString("Login_time", now.toString()) }
    }

    override fun getUserLoginTime(): String {
        return shared.getString("Login_time", "0")!!
    }




    //////address
    override fun saveUserLocation(address: String, postalCode: String) {
        shared.edit{putString("address",address) }
        shared.edit{putString("postalCode",postalCode) }
    }

    override fun getUserLocation(): Pair<String, String> {

        val address=shared.getString("address","Click To Add")
        val postalCode = shared.getString("postalCode","Click To Add")

        return Pair(address!!,postalCode!!)
    }


    override fun signOut() {

        TokenInMemory.refreshToken(null, null)
        shared.edit { clear() }
    }


}
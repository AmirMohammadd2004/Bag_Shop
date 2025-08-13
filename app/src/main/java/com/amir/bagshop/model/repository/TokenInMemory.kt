package com.amir.bagshop.model.repository

object TokenInMemory {

    var token: String? = null
        private set


    var username: String? = null
        private set

    fun refreshToken(newToken: String?, username: String?) {

        this.token = newToken
        this.username = username
    }
}
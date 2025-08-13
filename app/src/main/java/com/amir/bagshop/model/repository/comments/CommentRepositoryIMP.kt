package com.amir.bagshop.model.repository.comments

import com.amir.bagshop.model.data.Comment
import com.amir.bagshop.model.net.ApiService
import com.google.gson.JsonObject

class CommentRepositoryIMP(
    private val apiService: ApiService
) : CommentRepository {


    override suspend fun getAllComments(productId: String): List<Comment> {


        val jsonObject = JsonObject().apply {
            addProperty("productId", productId)
        }

        val data = apiService.getAllComments(jsonObject)

        if (data.success) {
            return data.comments
        }

        return listOf()
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun pushComment(
        productId: String,
        text: String,
        isSuccess: (String) -> Unit
    ) {


        val jsonObject = JsonObject().apply {

            addProperty("productId",productId)
            addProperty("text",text)
        }

        val result = apiService.addComment(jsonObject)

        if (result.success) {

            isSuccess.invoke(result.message)
        } else {
            isSuccess.invoke("Comment Is Not added")
        }
    }
}
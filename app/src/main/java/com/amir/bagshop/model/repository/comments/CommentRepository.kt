package com.amir.bagshop.model.repository.comments

import com.amir.bagshop.model.data.Comment


interface CommentRepository {

    suspend fun getAllComments(productId: String): List<Comment>
    suspend fun pushComment(productId: String, text: String , isSuccess:(String)-> Unit)

}
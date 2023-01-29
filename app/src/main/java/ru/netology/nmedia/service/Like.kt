package ru.netology.nmedia.service

import com.google.gson.annotations.SerializedName

class Like (
    @SerializedName ("userId")
    val userId: Int,
    @SerializedName ("userName")
    val userName: String,
    @SerializedName ("postId")
    val postId: Int,
    @SerializedName ("postAuthor")
    val postAuthor: String
)
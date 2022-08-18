package ru.netology.nmedia.service

import com.google.gson.annotations.SerializedName

class NewPost(
    @SerializedName("postAuthor")
    val postAuthor : String,
    @SerializedName("postContent")
    val postContent : String
)
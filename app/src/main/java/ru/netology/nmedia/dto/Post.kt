package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val content: String,
    val published: String,
    var shareCount: Int = 100,
    var likes: Int = 50,
    var likedByMe: Boolean = false
)
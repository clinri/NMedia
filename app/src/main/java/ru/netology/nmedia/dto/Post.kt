package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val content: String,
    val published: String,
    val shareCount: Int = 1099999,
    val likes: Int = 10_000,
    val likedByMe: Boolean = true,
    val video: String = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
)
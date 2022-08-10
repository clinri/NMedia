package ru.netology.nmedia.db

import ru.netology.nmedia.dto.Post

internal fun PostEntity.toModel() = Post(
    id = id,
    author = author,
    content = content,
    published = published,
    shareCount = shareCount,
    likes = likes,
    likedByMe = likedByMe,
    video = video
)

internal fun Post.toEntity() = PostEntity(
    id = id,
    author = author,
    content = content,
    published = published,
    shareCount = shareCount,
    likes = likes,
    likedByMe = likedByMe,
    video = video
)
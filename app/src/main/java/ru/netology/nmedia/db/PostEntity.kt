package ru.netology.nmedia.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "posts")
class PostEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "published")
    val published: String,
    @ColumnInfo(name = "share_count")
    val shareCount: Int,
    @ColumnInfo(name = "likes")
    val likes: Int,
    @ColumnInfo(name = "liked_by_me")
    val likedByMe: Boolean,
    @ColumnInfo(name = "video")
    val video: String
)
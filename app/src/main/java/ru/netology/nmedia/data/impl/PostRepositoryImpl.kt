package ru.netology.nmedia.data.impl

import androidx.lifecycle.map
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.db.PostEntity
import ru.netology.nmedia.db.toEntity
import ru.netology.nmedia.db.toModel
import ru.netology.nmedia.dto.Post

class PostRepositoryImpl(
    private val dao: PostDao
) : PostRepository {

    override val data = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }

    override fun insert(post: Post) {
        dao.insert(post.toEntity())
    }

    override fun updateContentById(post: Post) {
        dao.updateContentById(post.id,post.content)
    }

    override fun like(postId: Int) {
        dao.likeById(postId)
    }

    override fun share(postId: Int) {
        dao.shareById(postId)
    }

    override fun delete(postId: Int) {
        dao.removeById(postId)
    }
}
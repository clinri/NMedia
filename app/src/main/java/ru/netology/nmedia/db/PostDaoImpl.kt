package ru.netology.nmedia.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dto.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    override fun getAll() = db.query(
        PostsTable.NAME,
        PostsTable.ALL_COLUMNS_NAMES,
        null, null, null, null,
        "${PostsTable.Column.ID.columnName} DESC"
    ).use { cursor ->
        List(cursor.count) {
            cursor.moveToNext()
            cursor.toPost()
        }
    }


    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostsTable.Column.AUTHOR.columnName, post.author)
            put(PostsTable.Column.CONTENT.columnName, post.content)
            put(PostsTable.Column.PUBLISHED.columnName, post.published)
            put(PostsTable.Column.SHARE_COUNT.columnName, post.shareCount)
            put(PostsTable.Column.LIKED_BY_ME.columnName, post.likedByMe)
            put(PostsTable.Column.LIKES.columnName, post.likes)
        }
        val id = if (post.id != 0) {
            db.update(
                PostsTable.NAME,
                values,
                "${PostsTable.Column.ID.columnName} = ?",
                arrayOf(post.id.toString())
            )
            post.id
        } else { // post.id == 0
            db.insert(PostsTable.NAME, null, values)
        }
        return db.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUMNS_NAMES,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(id.toString()), null, null, null
        ).use { cursor ->
            cursor.moveToNext()
            cursor.toPost()
        }
    }

    override fun likeById(id: Int) {
        db.execSQL(
            """
                UPDATE ${PostsTable.NAME} SET
                    ${PostsTable.Column.LIKES.columnName} = ${PostsTable.Column.LIKES.columnName} + CASE WHEN likedByMe THEN -1 ELSE 1 END,
                    ${PostsTable.Column.LIKED_BY_ME.columnName} = CASE WHEN ${PostsTable.Column.LIKED_BY_ME.columnName} THEN 0 ELSE 1 END
                WHERE ${PostsTable.Column.ID.columnName} = ?;                
                """.trimIndent(),
            arrayOf(id)
        )
    }

    override fun shareById(id: Int) {
        db.execSQL(
            """
                UPDATE ${PostsTable.NAME} SET
                    ${PostsTable.Column.SHARE_COUNT.columnName} = ${PostsTable.Column.SHARE_COUNT.columnName} + 1                 
                WHERE ${PostsTable.Column.ID.columnName} = ?;                
                """.trimIndent(),
            arrayOf(id)
        )
    }

    override fun removeById(id: Int) {
        db.delete(
            PostsTable.NAME,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(id.toString())
        )
    }
}
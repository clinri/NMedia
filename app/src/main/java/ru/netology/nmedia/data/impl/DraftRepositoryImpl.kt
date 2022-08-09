package ru.netology.nmedia.data.impl

import ru.netology.nmedia.data.DraftRepository
import ru.netology.nmedia.dto.Draft

class DraftRepositoryImpl : DraftRepository {
    override var draft = Draft(
        isLock = true,
        content = ""
    )

    override fun Lock() {
        draft = draft.copy(isLock = true)
    }

    override fun unLock() {
        draft = draft.copy(isLock = false)
    }

    override fun getLockStatus(): Boolean {
        return draft.isLock
    }

    override fun setContent(content: String) {
        if (!draft.isLock) {
            draft = draft.copy(content = content)
        }
    }

    override fun getContent(): String {
        return if (!draft.isLock) {
            draft.content
        } else ""
    }

    override fun clearContent() {
        draft = draft.copy(content = "")
    }
}
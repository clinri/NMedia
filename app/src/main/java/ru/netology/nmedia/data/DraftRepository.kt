package ru.netology.nmedia.data

import ru.netology.nmedia.dto.Draft

interface DraftRepository{
    val draft:Draft
    fun Lock()
    fun unLock()
    fun getLockStatus():Boolean
    fun setContent(content: String)
    fun getContent():String
    fun clearContent()
}
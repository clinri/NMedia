package ru.netology.nmedia.util

import java.lang.Exception

class ItemNotFoundExceptions(
    textException: String = "Post not found"
) : Exception(textException)
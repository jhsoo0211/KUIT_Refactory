package com.konkuk.moru.core.datastore

import java.util.concurrent.ConcurrentHashMap

object LikeMemory {
    private val likedMap = ConcurrentHashMap<String, Boolean>()

    fun get(routineId: String): Boolean? = likedMap[routineId]
    fun set(routineId: String, isLiked: Boolean) { likedMap[routineId] = isLiked }
}
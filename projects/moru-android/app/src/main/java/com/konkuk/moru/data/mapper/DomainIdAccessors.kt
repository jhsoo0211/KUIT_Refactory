package com.konkuk.moru.data.mapper

import com.konkuk.moru.data.model.Routine

/** 서버 호출이 필요할 때: 도메인 객체 → 서버 String ID */
fun Routine.serverRoutineId(): String = routineId
fun Routine.serverAuthorId(): String = authorId

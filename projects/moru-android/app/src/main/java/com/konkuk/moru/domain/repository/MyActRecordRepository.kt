package com.konkuk.moru.domain.repository

import com.konkuk.moru.domain.model.MyActRecord
import com.konkuk.moru.domain.model.MyActRecordDetail
import com.konkuk.moru.domain.model.MyActRecordPage

interface MyActRecordRepository {
    suspend fun getTodayLogs(): List<MyActRecord>

    suspend fun getRecentLogs(): List<MyActRecord>

    suspend fun getLogs(createdAt: String? = null, logId: String? = null, size: Int? = null): MyActRecordPage

    suspend fun getLogDetail(id: String): MyActRecordDetail
}

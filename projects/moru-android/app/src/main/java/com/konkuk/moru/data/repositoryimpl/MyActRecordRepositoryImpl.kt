package com.konkuk.moru.data.repositoryimpl

import com.konkuk.moru.data.mapper.toDomain
import com.konkuk.moru.data.service.MyActLogService
import com.konkuk.moru.domain.model.MyActRecord
import com.konkuk.moru.domain.model.MyActRecordDetail
import com.konkuk.moru.domain.model.MyActRecordPage
import com.konkuk.moru.domain.repository.MyActRecordRepository
import javax.inject.Inject

class MyActRecordRepositoryImpl @Inject constructor(
    private val service: MyActLogService
) : MyActRecordRepository {
    override suspend fun getTodayLogs(): List<MyActRecord> =
        service.getTodayLogs().map { it.toDomain() }

    override suspend fun getRecentLogs(): List<MyActRecord> =
        service.getRecentLogs().map { it.toDomain() }

    override suspend fun getLogs(createdAt: String?, logId: String?, size: Int?): MyActRecordPage =
        service.getLogs(createdAt, logId, size).toDomain()

    override suspend fun getLogDetail(id: String): MyActRecordDetail =
        service.getLogDetail(id).toDomain()
}

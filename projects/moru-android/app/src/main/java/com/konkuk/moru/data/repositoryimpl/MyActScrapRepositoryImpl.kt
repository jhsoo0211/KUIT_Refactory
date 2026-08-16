package com.konkuk.moru.data.repositoryimpl

import com.konkuk.moru.data.mapper.toDomain
import com.konkuk.moru.data.service.MyActSocialService
import com.konkuk.moru.domain.repository.MyActScrapRepository
import javax.inject.Inject

class MyActScrapRepositoryImpl @Inject constructor(
    private val service: MyActSocialService
): MyActScrapRepository {
    override suspend fun getScraps(createdAt: String?, scrapId: String?, size: Int?)
            = service.getScraps(createdAt, scrapId, size).toDomain()
}
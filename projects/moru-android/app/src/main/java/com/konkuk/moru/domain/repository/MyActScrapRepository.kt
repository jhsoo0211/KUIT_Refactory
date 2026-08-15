package com.konkuk.moru.domain.repository

import com.konkuk.moru.data.mapper.toDomain
import com.konkuk.moru.data.service.MyActSocialService
import com.konkuk.moru.domain.model.MyActScrapsPage
import javax.inject.Inject

interface MyActScrapRepository {
    suspend fun getScraps(createdAt: String? = null, scrapId: String? = null, size: Int? = null): MyActScrapsPage
}


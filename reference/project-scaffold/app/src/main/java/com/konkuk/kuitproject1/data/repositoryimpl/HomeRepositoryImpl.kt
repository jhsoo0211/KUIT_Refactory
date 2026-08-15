package com.konkuk.kuitproject1.data.repositoryimpl

import com.konkuk.kuitproject1.data.mapper.toHomeModel
import com.konkuk.kuitproject1.data.service.HomeService
import com.konkuk.kuitproject1.domain.entity.HomeModel
import com.konkuk.kuitproject1.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeService: HomeService
) : HomeRepository {
    override suspend fun getHomeData(request: Int): Result<HomeModel> =
        runCatching {
            val respose = homeService.getHomeData(request)
            respose.data.toHomeModel()
        }
    }



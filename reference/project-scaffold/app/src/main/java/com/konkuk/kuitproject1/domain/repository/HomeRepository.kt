package com.konkuk.kuitproject1.domain.repository

import com.konkuk.kuitproject1.domain.entity.HomeModel

interface HomeRepository {
    suspend fun getHomeData(request:Int):Result<HomeModel>
}
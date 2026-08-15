package com.konkuk.kuitproject1.data.mapper

import com.konkuk.kuitproject1.data.dto.respose.ResposeHomeDto
import com.konkuk.kuitproject1.domain.entity.HomeModel

fun ResposeHomeDto.toHomeModel()=HomeModel(
    homeTitle=homeTitle,
    homeSubTitle=homeSubTitle
)
package com.example.kuitandroidapiexample.model

import com.example.kuitandroidapiexample.data.dto.ResponseAnimalDto
import com.example.kuitandroidapiexample.data.dto.response.ResponseAnimalDetailDto


data class UiAnimal(
    val id: Int,
    val name: String,
    val url: String,
    val state: AnimalType,
    val breed: String,
    val address: String,
    val reporter: String?
)

fun ResponseAnimalDto.toUi() = UiAnimal(
    id       = id,
    name     = name,
    url      = url,
    state    = AnimalType.valueOf(state),   // 문자열 → enum
    breed    = breed,
    address  = address,
    reporter = null                         // 목록에는 신고자 정보가 없음
)


fun ResponseAnimalDetailDto.toUi() = UiAnimal(
    id       = id,
    name     = name,
    url      = url,
    state    = AnimalType.valueOf(state),
    breed    = breed,
    address  = address,
    reporter = reporterName                // 서버 JSON의 description 필드
)
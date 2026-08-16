package com.konkuk.moru.presentation.home

data class RoutineStepData(
    val name: String, //루틴명
    val duration: Int, //소요시간
    var isChecked: Boolean = true //실행유무
)
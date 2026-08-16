package com.konkuk.moru.presentation.onboarding.model

import com.konkuk.moru.data.model.TagDef

object OnboardingTags {
    val SITUATION = listOf(
        TagDef("#출근길"), TagDef("#지하철"), TagDef("#퇴근길"),
        TagDef("#모닝루틴"), TagDef("#일어나서"), TagDef("#저녁"),
        TagDef("#자기전"), TagDef("#휴일"), TagDef("#공강"),
    )
    val ACTIVITY = listOf(
        TagDef("#독서"), TagDef("#과제"), TagDef("#공부"),
        TagDef("#작업"), TagDef("#다이어트"), TagDef("#수능"),
        TagDef("#취준"), TagDef("#프로그래밍"), TagDef("#휴식"),
    )
}
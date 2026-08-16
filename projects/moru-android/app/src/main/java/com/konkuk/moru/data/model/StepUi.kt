package com.konkuk.moru.data.model

import java.util.UUID

data class StepUi(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val time: String = ""
)

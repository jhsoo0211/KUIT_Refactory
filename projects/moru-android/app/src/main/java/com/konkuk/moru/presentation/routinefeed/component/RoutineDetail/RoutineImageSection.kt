package com.konkuk.moru.presentation.routinefeed.component.RoutineDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import coil3.compose.AsyncImage

@Composable
fun RoutineImageSection(imageUrl: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // 1:1 비율
            .background(Color(0xFFF1F3F5)), // 연한 회색 배경
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isNullOrBlank()) {
            // 이미지 URL이 없을 때 기본 아이콘 표시
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = "이미지 없음",
                modifier = Modifier.size(64.dp),
                tint = Color.LightGray
            )
        } else {
            // 이미지 URL이 있을 때 네트워크 이미지 로드 (Coil 라이브러리 사용)
            AsyncImage(
                model = imageUrl,
                contentDescription = "루틴 대표 이미지",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // 이미지를 꽉 채우도록 설정
            )
        }
    }
}


@Preview(showBackground = true, name = "Image Section (With Image)")
@Composable
fun RoutineImageSectionPreview_WithImage() {
    RoutineImageSection(
        imageUrl = "https://images.unsplash.com/photo-1542744173-8e7e53415bb0" // 예시 이미지 URL
    )
}

/**
 * RoutineImageSection 프리뷰
 * 2. 이미지가 없을 경우 (null 또는 blank)
 */
@Preview(showBackground = true, name = "Image Section (No Image)")
@Composable
fun RoutineImageSectionPreview_NoImage() {
    RoutineImageSection(
        imageUrl = null
    )
}
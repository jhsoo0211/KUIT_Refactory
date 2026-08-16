package com.konkuk.moru.presentation.routinefeed.component.modale

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.konkuk.moru.ui.theme.MORUTheme

/**
 * 정보 전달용 중앙 정렬 다이얼로그
 * 이전 다이얼로그와 크기가 동일하며, 내용만 중앙에 표시됩니다.
 *
 * @param onDismissRequest 다이얼로그가 닫혀야 할 때 호출되는 함수
 * @param dialogColor 다이얼로그의 배경 색상
 * @param content 다이얼로그 중앙에 표시될 메인 컨텐츠 (Composable)
 */
@Composable
fun CenteredInfoDialog(
    onDismissRequest: () -> Unit,
    dialogColor: Color = Color(0xFF2C2C2C),
    content: @Composable () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            // 이전 다이얼로그와 동일한 크기 설정
            modifier = Modifier.size(width = 264.dp, height = 80.dp),
            shape = RoundedCornerShape(14.dp),
            color = dialogColor
        ) {
            // Box를 사용하여 컨텐츠를 수평/수직 중앙에 배치
            Box(
                modifier = Modifier.fillMaxSize(), // Surface를 꽉 채움
                contentAlignment = Alignment.Center // 내용물을 중앙에 정렬
            ) {
                content()
            }
        }
    }
}

@Preview(name = "Centered Info Dialog")
@Composable
fun CenteredInfoDialogPreview() {
    CenteredInfoDialog(
        onDismissRequest = {},
        dialogColor = Color(0xFF3A3A3A),
    ) {
        Text(text = "수정되었습니다!", color = Color(0xFFE0E0E0), style = MORUTheme.typography.desc_M_14)
    }
}
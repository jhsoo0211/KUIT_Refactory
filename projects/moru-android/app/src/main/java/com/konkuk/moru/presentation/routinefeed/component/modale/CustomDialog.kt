package com.konkuk.moru.presentation.routinefeed.component.modale
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.konkuk.moru.core.component.button.MoruButton
import com.konkuk.moru.ui.theme.MORUTheme

@Composable
fun CustomDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    // ❗️ content를 Composable 람다로 다시 받아서 유연성 확보
    content: @Composable () -> Unit,
    showTwoButtons: Boolean = true,
    confirmButtonText: String = "확인",
    dismissButtonText: String = "취소"
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.size(width = 280.dp, height = 150.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color.White // ❗️ 흰색 배경으로 변경
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // --- 상단 컨텐츠 (Composable 람다 호출) ---
                // Box를 사용해 컨텐츠가 중앙에 오도록 함
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    content()
                }

                // --- 하단 버튼 ---
                if (showTwoButtons) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MoruButton(
                            modifier = Modifier.weight(1f).height(48.dp),
                            text = dismissButtonText,
                            onClick = onDismissRequest,
                            backgroundColor = MORUTheme.colors.lightGray,
                            contentColor = MORUTheme.colors.mediumGray
                        )
                        MoruButton(
                            modifier = Modifier.weight(1f).height(48.dp),
                            text = confirmButtonText,
                            onClick = onConfirmation,
                            backgroundColor = MORUTheme.colors.limeGreen,
                            contentColor = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Custom Dialog Preview")
@Composable
private fun CustomDialogCustomTextPreview() {
    MORUTheme {
        CustomDialog(
            onConfirmation = {},
            onDismissRequest = {},
            // ❗️ content 람다 안에 원하는 UI 요소를 넣는 방식으로 변경
            content = {
                Text(
                    text = "루틴을 삭제하시겠습니까?",
                    style = MORUTheme.typography.title_B_20,
                    textAlign = TextAlign.Center
                )
            }
        )
    }
}
package com.konkuk.moru.core.component.chip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme


/**
 * Moru 디자인 시스템의 칩 (태그, 필터)
 *
 * @param text 칩 텍스트
 * @param onClick 클릭 이벤트
 * @param isSelected 선택 상태 여부
 * @param border 테두리 스타일. 기본값은 null
 * @param selectedBackgroundColor 선택 시 배경색
 * @param selectedContentColor 선택 시 내용 색상
 * @param unselectedBackgroundColor 미선택 시 배경색
 * @param unselectedContentColor 미선택 시 내용 색상
 * @param modifier Modifier
 * @param shape 칩 모양 (기본값: 알약 모양)
 * @param textStyle 텍스트 스타일. 지정하지 않으면 MORUTheme의 기본 스타일이 적용됩니다.
 * @param startIconContent 텍스트 앞에 표시될 아이콘 (Nullable)
 * @param endIconContent 텍스트 뒤에 표시될 아이콘 (Nullable)
 * @param contentPadding 내부 콘텐츠의 패딩 값
 */
@Composable
fun MoruChip(
    text: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    selectedBackgroundColor: Color,
    selectedContentColor: Color,
    unselectedBackgroundColor: Color,
    unselectedContentColor: Color,
    modifier: Modifier = Modifier,
    border: BorderStroke? = null,
    shape: Shape = CircleShape,
    textStyle: TextStyle? = null,
    startIconContent: (@Composable () -> Unit)? = null,
    endIconContent: (@Composable () -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
) {
    val backgroundColor = if (isSelected) selectedBackgroundColor else unselectedBackgroundColor
    val contentColor = if (isSelected) selectedContentColor else unselectedContentColor

    Surface(
        modifier = modifier
            .clip(shape)
            .clickable(onClick = onClick),
        shape = shape,
        color = backgroundColor,
        contentColor = contentColor,
        border = border
    ) {
        Row(
            modifier = Modifier.padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // 시작 아이콘
            if (startIconContent != null) {
                startIconContent()
                Spacer(modifier = Modifier.width(4.dp))
            }

            Text(
                text = text,
                style = textStyle ?: MORUTheme.typography.body_SB_14 // textStyle이 null이면 기본값 사용
            )

            // 끝 아이콘
            if (endIconContent != null) {
                Spacer(modifier = Modifier.width(4.dp))
                endIconContent()
            }
        }
    }
}

@Preview(showBackground = true, name = "MoruChip 아이콘 배치 예시")
@Composable
private fun MoruChipWithIconsPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("--- 아이콘 예시 ---", fontWeight = FontWeight.Bold)

        // 1. 아이콘이 없는 기본 칩
        MoruChip(
            text = "#기본태그",
            isSelected = true,
            onClick = { },
            selectedBackgroundColor = MORUTheme.colors.paleLime,
            selectedContentColor = MORUTheme.colors.oliveGreen,
            unselectedBackgroundColor = MORUTheme.colors.veryLightGray,
            unselectedContentColor = MORUTheme.colors.darkGray
        )

        // 2. 시작 아이콘만 있는 칩 (수정됨)
        MoruChip(
            text = "선택됨",
            isSelected = false,
            onClick = { },
            selectedBackgroundColor = Color.Black,
            selectedContentColor = Color.White,
            unselectedBackgroundColor = Color.White,
            unselectedContentColor = Color.Black,
            startIconContent = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    modifier = Modifier.size(16.dp)
                )
            }
        )

        // 3. 끝 아이콘만 있는 칩
        MoruChip(
            text = "#삭제가능",
            isSelected = true,
            onClick = { },
            selectedBackgroundColor = Color.Black,
            selectedContentColor = MORUTheme.colors.limeGreen,
            unselectedBackgroundColor = Color.White,
            unselectedContentColor = Color.Black,
            endIconContent = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    modifier = Modifier.size(14.dp)
                )
            }
        )

        // 4. 양쪽 아이콘이 모두 있는 칩
        MoruChip(
            text = "모든아이콘",
            isSelected = true,
            onClick = { },
            selectedBackgroundColor = Color.Black,
            selectedContentColor = Color.White,
            unselectedBackgroundColor = Color.White,
            unselectedContentColor = Color.Black,
            startIconContent = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Status",
                    modifier = Modifier.size(18.dp)
                )
            },
            endIconContent = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier.size(18.dp)
                )
            }
        )

        Text("--- 테두리 예시 ---", fontWeight = FontWeight.Bold)

        // 5. 테두리가 있는 칩 (미선택 상태)
        MoruChip(
            text = "미완료",
            isSelected = false,
            onClick = { },
            selectedBackgroundColor = MORUTheme.colors.oliveGreen,
            selectedContentColor = Color.White,
            unselectedBackgroundColor = Color.White,
            unselectedContentColor = MORUTheme.colors.oliveGreen,
            border = BorderStroke(width = 1.dp, color = MORUTheme.colors.limeGreen)
        )

        // 6. 테두리가 없는 칩 (선택 상태)
        MoruChip(
            text = "완료",
            isSelected = true,
            onClick = { },
            selectedBackgroundColor = MORUTheme.colors.oliveGreen,
            selectedContentColor = Color.White,
            unselectedBackgroundColor = Color.White,
            unselectedContentColor = MORUTheme.colors.oliveGreen,
            border = null
        )
    }
}
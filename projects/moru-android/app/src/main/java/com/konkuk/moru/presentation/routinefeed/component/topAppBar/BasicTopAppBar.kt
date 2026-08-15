package com.konkuk.moru.presentation.routinefeed.component.topAppBar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konkuk.moru.ui.theme.moruFontSemiBold

/**
 * 가장 기본적인 형태의 TopAppBar
 * @param title 중앙에 표시될 텍스트
 * @param navigationIcon 왼쪽에 표시될 아이콘 컴포저블 (뒤로가기 등)
 * @param spacingBetweenIconAndTitle 아이콘과 타이틀 사이의 간격
 * @param actions 오른쪽에 표시될 아이콘 리스트 컴포저블
 * @param modifier Modifier
 * @param colors 앱 바의 배경색, 글자색 등을 지정
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTopAppBar(
    title: String,
    navigationIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable (RowScope.() -> Unit) = {},
    spacingBetweenIconAndTitle: Dp = 7.dp, // ◀ 1. 파라미터 추가 및 기본값 설정
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.White,
        titleContentColor = Color.Black,
        navigationIconContentColor = Color.Black,
        actionIconContentColor = Color.Black
    ),
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge
) {
    TopAppBar(
        // ◀ 2. title 슬롯을 Row로 재구성하여 간격 제어
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // 전달받은 내비게이션 아이콘을 여기에 배치
                navigationIcon()
                // 전달받은 간격으로 Spacer 추가
                Spacer(modifier = Modifier.width(spacingBetweenIconAndTitle))
                // 타이틀 텍스트 배치
                Text(text = title, style = titleStyle, fontFamily = moruFontSemiBold,fontSize=16.sp)
            }
        },
        // ◀ 3. 기존 navigationIcon 슬롯은 비워둡니다.
        navigationIcon = {},
        modifier = modifier,
        actions = actions,
        colors = colors
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun BasicTopAppBarPreview() {
    MaterialTheme {
        BasicTopAppBar(
            title = "루틴명을 검색해보세요",
            navigationIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "뒤로가기",
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            // 프리뷰에서도 간격 테스트 가능
            spacingBetweenIconAndTitle = 20.dp
        )
    }
}

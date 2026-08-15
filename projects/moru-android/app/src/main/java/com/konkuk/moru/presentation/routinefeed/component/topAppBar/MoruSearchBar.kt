package com.konkuk.moru.presentation.routinefeed.component.topAppBar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 인터랙티브 검색 바 컴포넌트
 * @param query 현재 검색어
 * @param onQueryChange 검색어가 변경될 때 호출되는 콜백
 * @param onSearch 검색 실행 시(키보드 검색 버튼 클릭) 호출되는 콜백
 * @param onClick 컴포넌트 클릭 시 동작. null이 아니면 입력이 비활성화되고 클릭 이벤트만 처리.
 */
@Composable
fun MoruSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null // ◀◀◀ 클릭 이벤트를 위한 파라미터 추가
) {
    val focusManager = LocalFocusManager.current

    // 공통 스타일 정의
    val searchBarModifier = Modifier
        .fillMaxWidth()
        //.width(201.dp)
        .height(32.dp)
        .background(
            color = Color(0xFF595959),
            shape = RoundedCornerShape(size = 100.dp)
        )

    Box(modifier = modifier) {
        if (onClick != null) {
            // --- 1. 클릭 전용 UI (입력 비활성화) ---
            Box(
                modifier = searchBarModifier.clickable(onClick = onClick),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "루틴을 검색해 보세요!",
                    color = Color.LightGray.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        } else {
            // --- 2. 기존의 텍스트 입력 UI ---
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = searchBarModifier,
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp
                ),
                singleLine = true,
                cursorBrush = SolidColor(Color.White),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onSearch(query)
                    focusManager.clearFocus()
                }),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (query.isEmpty()) {
                            Text(
                                text = "루틴을 검색해 보세요!",
                                color = Color.LightGray.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}


@Preview(name = "입력 가능 모드")
@Composable
private fun MoruSearchBarInteractivePreview() {
    var text by remember { mutableStateOf("") }
    MoruSearchBar(
        query = text,
        onQueryChange = { text = it },
        onSearch = {}
    )
}

@Preview(name = "클릭 전용 모드")
@Composable
private fun MoruSearchBarClickablePreview() {
    MoruSearchBar(
        query = "",
        onQueryChange = {},
        onSearch = {},
        onClick = {}
    )
}

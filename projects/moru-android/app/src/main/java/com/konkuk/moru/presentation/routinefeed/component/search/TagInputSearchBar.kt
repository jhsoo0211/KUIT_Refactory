package com.konkuk.moru.presentation.routinefeed.component.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme

@Composable
internal fun TagInputSearchBar(
    modifier: Modifier = Modifier,
    selectedTags: List<String>,
    onDeleteTag: (String) -> Unit,
    onAddTag: (String) -> Unit,
    onNavigateToTagSearch: () -> Unit
) {
    var tagInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    fun submitTag() {
        if (tagInput.isNotBlank()) {
            val tag = if (tagInput.startsWith("#")) tagInput else "#$tagInput"
            onAddTag(tag)
            tagInput = ""
            focusManager.clearFocus()
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(100.dp))
            .background(MORUTheme.colors.veryLightGray)
            .border(1.dp, MORUTheme.colors.lightGray, RoundedCornerShape(100.dp))
            .padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        /* ---------- ① 태그 + 입력창 : 남는 공간 전부 ---------- */
        LazyRow(
            modifier = Modifier
                .weight(1f)              // ★ 핵심: 아이콘을 제외한 모든 공간 차지
                .padding(end = 8.dp),    // 아이콘과 살짝 간격
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /* 선택된 태그 칩 */
            items(selectedTags) { tag ->
                SelectedTagChip(text = tag) { onDeleteTag(tag) }
            }

            /* 입력창 */
            item {
                BasicTextField(
                    value = tagInput,
                    onValueChange = { tagInput = it },
                    singleLine = true,
                    textStyle = MORUTheme.typography.time_R_14
                        .copy(color = MORUTheme.colors.black),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions { submitTag() },
                    decorationBox = { inner ->
                        if (tagInput.isEmpty() && selectedTags.isEmpty()) {
                            Text(
                                text = "#태그 검색",
                                style = MORUTheme.typography.time_R_14,
                                color = MORUTheme.colors.mediumGray
                            )
                        }
                        inner()
                    }
                )
            }
        }

        /* ---------- ② “+” 아이콘 : 항상 오른쪽 ---------- */
        IconButton(
            onClick = onNavigateToTagSearch,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "태그 추가",
                tint = MORUTheme.colors.mediumGray
            )
        }
    }
}

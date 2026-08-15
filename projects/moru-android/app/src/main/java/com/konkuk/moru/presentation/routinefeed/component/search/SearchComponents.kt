package com.konkuk.moru.presentation.routinefeed.component.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.core.component.chip.MoruChip
import com.konkuk.moru.presentation.myactivity.screen.TagDto

import com.konkuk.moru.ui.theme.MORUTheme

// --- Data Transfer Object for Tags ---


fun generateDummyTags(): List<TagDto> {
    val sampleTagWords = listOf(
        "자바", "코틀린", "파이썬", "웹", "앱", "프론트", "백엔드", "데이터",
        "AI", "알고", "디자인", "UX", "UI", "서버", "DB", "리액트", "뷰", "안드로",
        "IOS", "기획", "보안", "게임", "클라우드", "머신", "딥러닝", "마케팅",
        "영상", "블록", "핀테크", "스타트"
    )
    val tags = sampleTagWords.mapIndexed { index, word -> TagDto(id = index, name = "#$word") }
    val selected = tags.shuffled().take(8)
    return tags.map { tag -> selected.find { it.id == tag.id }?.copy(isSelected = true) ?: tag }
}


@Composable
internal fun TagSectionHeader(title: String) {
    Text(text = title, style = MORUTheme.typography.body_SB_14)
}

@Composable
internal fun TagChip(
    text: String,
    selected: Boolean,
    showCloseIcon: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) MORUTheme.colors.paleLime else Color(0xFFF5F6F8)
    val textColor = if (selected) MORUTheme.colors.oliveGreen else MORUTheme.colors.mediumGray
    val borderModifier = if (selected) Modifier.border(
        1.dp,
        MORUTheme.colors.oliveGreen,
        RoundedCornerShape(140.dp)
    ) else Modifier

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(140.dp))
            .then(borderModifier)
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 13.dp, vertical = 8.dp)
    ) {
        Text(text = text, style = MORUTheme.typography.time_R_14, color = textColor)
        if (showCloseIcon) {
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}


/**
 * 검색 결과 화면에서 선택된 태그를 표시하는 칩
 * @param text 표시될 태그 텍스트
 * @param onDeleteClick 'X' 버튼 클릭 시 동작
 */
@Composable
internal fun SelectedTagChip(
    text: String,
    onDeleteClick: () -> Unit
) {
    MoruChip(
        text = text,
        onClick = {}, // 칩 자체에는 클릭 효과 없음
        isSelected = true,
        selectedBackgroundColor = MORUTheme.colors.charcoalBlack,
        selectedContentColor = MORUTheme.colors.limeGreen,
        unselectedBackgroundColor = Color.Transparent, // 사용되지 않음
        unselectedContentColor = Color.Transparent, // 사용되지 않음
        textStyle = MORUTheme.typography.time_R_14,
        contentPadding = PaddingValues(start = 12.dp, top = 6.dp, bottom = 6.dp, end = 4.dp),
        endIconContent = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "태그 삭제",
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onDeleteClick() } // 아이콘을 클릭해서 삭제
            )
        }
    )
}



@Preview
@Composable
fun TagInputSearchBarPreview() {
    TagInputSearchBar(
        selectedTags = listOf(),
        onNavigateToTagSearch = {},
        onAddTag = {},
        onDeleteTag = {})
}

@Preview
@Composable
fun SelectedTagChipPreview() {
    SelectedTagChip(text = "", onDeleteClick = {})
}
package com.konkuk.moru.core.component.routinedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.core.component.chip.MoruChip
import com.konkuk.moru.ui.theme.MORUTheme


@Composable
fun MyRoutineTag(
    tags: List<String>,
    isEditMode: Boolean,
    onAddTag: () -> Unit,
    onDeleteTag: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 17.dp, end = 17.dp, top = 20.dp, bottom = 30.dp),
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(tags) { tag ->
                MoruChip(
                    modifier =Modifier.height(31.dp),
                    text = "#$tag",
                    isSelected = true,
                    onClick = {
                        if (isEditMode) {
                            onDeleteTag(tag)
                        }
                    },
                    selectedBackgroundColor = Color.Black,
                    selectedContentColor = MORUTheme.colors.limeGreen,
                    unselectedBackgroundColor = Color.White,
                    unselectedContentColor = Color.Black,
                    endIconContent = if (isEditMode) {
                        {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove Tag",
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    } else null
                )
            }

            val canAddMore = isEditMode && tags.size < 3

            if (canAddMore) {
                item {
                    IconButton(
                        onClick = onAddTag,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(31.dp)
                                .background(
                                    color = MORUTheme.colors.lightGray,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(31.dp),
                                painter = painterResource(id = R.drawable.ic_routinetagplus),
                                tint = Color.Unspecified,
                                contentDescription = "태그 추가",

                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, name = "태그 정보 - 보기 모드")
@Composable
private fun RoutineInfoPreview_ViewMode() {
    MORUTheme {
        MyRoutineTag(
            tags = listOf("아침루틴", "운동"),
            isEditMode = false, // 보기 모드
            onAddTag = {},
            onDeleteTag = {}
        )
    }
}

@Preview(showBackground = true, name = "태그 정보 - 보기 모드")
@Composable
private fun RoutineInfoPreview_ViewMode2() {
    MORUTheme {
        MyRoutineTag(
            tags = listOf("아침루틴", "운동", "건강"),
            isEditMode = true, // 보기 모드
            onAddTag = {},
            onDeleteTag = {}
        )
    }
}

@Preview(showBackground = true, name = "태그 정보 - 보기 모드")
@Composable
private fun RoutineInfoPreview_ViewMode3() {
    MORUTheme {
        MyRoutineTag(
            tags = listOf("아침루틴", "운동"),
            isEditMode = true, // 보기 모드
            onAddTag = {},
            onDeleteTag = {}
        )
    }
}
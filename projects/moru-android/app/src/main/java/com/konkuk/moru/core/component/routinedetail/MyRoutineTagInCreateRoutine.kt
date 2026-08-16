package com.konkuk.moru.core.component.routinedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.core.component.chip.AddTagButton
import com.konkuk.moru.core.component.chip.InitialAddTagChip
import com.konkuk.moru.core.component.chip.MoruChip
import com.konkuk.moru.core.component.chip.TagChip
import com.konkuk.moru.ui.theme.MORUTheme


@Composable
fun MyRoutineTagInCreateRoutine(
    tagList: List<String>,
    onAddTag: () -> Unit,
    onDeleteTag: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (tagList.isEmpty()) {
            item {
                InitialAddTagChip { onAddTag() }
            }
        }
        items(tagList) { tag ->
            TagChip(
                text = tag,
                onRemove = { onDeleteTag(tag) }
            )
        }

        if (tagList.isNotEmpty() && tagList.size < 3) {
            item {
                AddTagButton { onAddTag() }
            }
        }
    }
}


@Preview()
@Composable
private fun MyRoutineTagInCreateRoutinePreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        MyRoutineTagInCreateRoutine(
            tagList = listOf(),
            onAddTag = {},
            onDeleteTag = {}
        )
        MyRoutineTagInCreateRoutine(
            tagList = listOf("운동", "건강", "식단"),
            onAddTag = {},
            onDeleteTag = {}
        )
        MyRoutineTagInCreateRoutine(
            tagList = listOf("운동", "건강"),
            onAddTag = {},
            onDeleteTag = {}
        )
    }
}
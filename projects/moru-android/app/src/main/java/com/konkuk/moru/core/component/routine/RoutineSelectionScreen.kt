package com.konkuk.moru.core.component.routine

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konkuk.moru.R

@Composable
fun RoutineListItem(
    modifier: Modifier = Modifier,
    isRunning: Boolean,
    routineName: String,
    tags: List<String>,
    likeCount: Int,
    isLiked: Boolean,
    showCheckbox: Boolean = false,
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    onItemClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageResource = if (isRunning) R.drawable.ic_routine_square_running else R.drawable.ic_routine_square_stop
        Image(painter = painterResource(id = imageResource), contentDescription = routineName, modifier = Modifier.size(72.dp))

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = routineName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = tags.joinToString(" ") { "#$it" }, color = Color.Gray, fontSize = 12.sp)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                // 좋아요 클릭 영역 유지
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "좋아요",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray // 원본 색상 유지
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = likeCount.toString(),
                    color = if (isLiked) Color.Black else Color.Gray, // 원본 색상 유지
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        if (showCheckbox) {
            IconButton(onClick = { onCheckedChange(!isChecked) }) {
                val icon = if (isChecked) R.drawable.check_box else R.drawable.empty_box
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "Checkbox",
                    tint = Color.Unspecified, // 원본 아이콘 색상 유지
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// Preview는 수정할 필요가 없습니다.
@Preview(showBackground = true)
@Composable
private fun RoutineListItemPreview() {
    Column {
        RoutineListItem(
            isRunning = true,
            routineName = "아침 운동",
            tags = listOf("모닝루틴", "스트레칭"),
            likeCount = 16,
            isLiked = true,
            showCheckbox = true,
            isChecked = true,
            onItemClick = {}
        )
        RoutineListItem(
            isRunning = false,
            routineName = "저녁 독서",
            tags = listOf("취미", "자기계발"),
            likeCount = 32,
            isLiked = false,
            showCheckbox = true,
            isChecked = false,
            onItemClick = {}
        )
    }
}
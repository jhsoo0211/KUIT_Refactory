package com.konkuk.moru.core.component.routine

// ... 다른 import 구문들은 그대로 ...
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
fun RoutineListItemWithClock(
    modifier: Modifier = Modifier,
    isRunning: Boolean,
    routineName: String,
    tags: List<String>,
    likeCount: Int,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    onClockClick: () -> Unit,
    onItemClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick=onItemClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageResource = if (isRunning) R.drawable.ic_routine_square_running else R.drawable.ic_routine_square_stop
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = routineName,
            modifier = Modifier.size(72.dp)
        )

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = routineName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = tags.joinToString(" "), color = Color.Gray, fontSize = 12.sp)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                //modifier = Modifier.clickable { onLikeClick() }
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "좋아요",
                    modifier = Modifier.size(16.dp),
                    tint = if (isLiked) Color.Red else Color.Gray
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = likeCount.toString(),
                    color = if (isLiked) Color.Black else Color.Gray,
                    fontSize = 12.sp
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ic_clock),
            contentDescription = "시간 설정",
            modifier = Modifier
                .size(25.dp)
                .clickable { onClockClick() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RoutineListItemWithClockPreview2() {
    MaterialTheme {
        RoutineListItemWithClock(
            isRunning = true,
            routineName = "아침 스트레칭",
            tags = listOf("#모닝루틴", "#건강"),
            likeCount = 120,
            isLiked = true,
            onLikeClick = {},
            onClockClick = {},
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true, name = "시간 미설정")
@Composable
private fun RoutineListItemWithClockPreview1() {
    MaterialTheme {
        RoutineListItemWithClock(
            isRunning = true,
            routineName = "아침 운동",
            tags = listOf("#모닝루틴", "#스트레칭"),
            likeCount = 16,
            isLiked = true,
            // 시간이 설정되지 않은 경우
            onLikeClick = {},
            onClockClick = {},
            onItemClick = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "시간 설정")
@Composable
private fun RoutineListItemWithClockTimeSetPreview() {
    MaterialTheme {
        RoutineListItemWithClock(
            isRunning = false,
            routineName = "저녁 독서",
            tags = listOf("#취미", "#자기계발"),
            likeCount = 32,
            isLiked = false, // 오후 10:30
            onLikeClick = {},
            onClockClick = {},
            onItemClick = {}
        )
    }
}
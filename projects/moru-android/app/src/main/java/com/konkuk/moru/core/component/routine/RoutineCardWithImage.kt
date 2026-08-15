package com.konkuk.moru.core.component.routine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.moruFontMedium
import com.konkuk.moru.ui.theme.moruFontRegular

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                RoutineListWithImageScreen()
            }
        }
    }
}

@Composable
fun RoutineCardWithImage(
    modifier: Modifier = Modifier,
    isRunning: Boolean,
    routineName: String,
    tags: List<String>,
    likeCount: Int,
    isLiked: Boolean,
    //onLikeClick: () -> Unit,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .width(98.dp)
            .clickable { onClick() }
    ) {
        val imageResource = if (isRunning) {
            R.drawable.ic_routine_rectangle_running
        } else {
            R.drawable.ic_routine_rectangle_stop
        }

        Image(
            painter = painterResource(id = imageResource),
            contentDescription = routineName,
            modifier = Modifier
                .width(98.dp)
                .height(130.dp),
            //contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = routineName,
            fontWeight = FontWeight(500),
            fontFamily = moruFontMedium,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = tags.joinToString(" ") { "#$it" },
            fontFamily = moruFontRegular,
            maxLines = 2,                               // 최대 2줄
            overflow = TextOverflow.Ellipsis,          // 말줄임
            lineHeight = 12.sp,
            fontSize = 10.sp,
            fontWeight = FontWeight(400),
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(3.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            //modifier = Modifier.clickable { onLikeClick() }
        ) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "좋아요",
                tint =  Color.Gray,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = likeCount.toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight(400),
                color = if (isLiked) Color.Black else Color.Gray
            )
        }
    }
}

@Composable
fun RoutineListWithImageScreen() {
    // 1. 서버에서 받아온 초기 데이터 모델
    data class RoutineInfo(
        val id: String,
        val name: String,
        val tags: List<String>,
        val likes: Int,
        val isRunning: Boolean,
        val isLiked: Boolean
    )

    // 2. 서버에서 받았다고 가정한 샘플 데이터
    val routines = remember {
        listOf(
            // tag = "운동" -> tags = listOf("운동")
            RoutineInfo("routine-1", "매일 조깅하기", listOf("운동"), 16, isRunning = true, isLiked = true),
            // 여러 태그가 있는 경우
            RoutineInfo("routine-2", "아침 책읽기", listOf("독서", "자기계발"), 25, isRunning = false, isLiked = false),
            RoutineInfo("routine-3", "영어 공부", listOf("학습", "외국어"), 8, isRunning = true, isLiked = false),
            RoutineInfo("routine-4", "요리 배우기", listOf("취미", "쿠킹"), 112, isRunning = false, isLiked = true),
        )
    }

    // 3. '좋아요' 상태와 카운트를 UI에서 즉시 반영하기 위한 로컬 상태
    //    서버에서 받은 초기값으로 한 번만 초기화합니다.
    val likedStates = remember {
        mutableStateMapOf<String, Boolean>().apply {
            routines.forEach { put(it.id, it.isLiked) }
        }
    }
    val likeCounts = remember {
        mutableStateMapOf<String, Int>().apply {
            routines.forEach { put(it.id, it.likes) }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(
                text = "루틴카드 (좋아요 기능 적용)",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(16.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(routines) { routine ->
                    // UI에 표시할 값은 로컬 상태에서 가져옵니다.
                    val isLiked = likedStates[routine.id] ?: routine.isLiked
                    val currentLikeCount = likeCounts[routine.id] ?: routine.likes

                    RoutineCardWithImage(
                        isRunning = routine.isRunning,
                        routineName = routine.name,
                        tags = routine.tags,
                        likeCount = currentLikeCount,
                        isLiked = isLiked,
                        onClick = {
                            // TODO: 상세 화면으로 이동하는 로직 구현
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoutineListWithImageScreenPreview() {
    MaterialTheme {
        RoutineListWithImageScreen()
    }
}

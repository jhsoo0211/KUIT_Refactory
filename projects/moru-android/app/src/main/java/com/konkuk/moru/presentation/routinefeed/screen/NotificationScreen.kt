package com.konkuk.moru.presentation.routinefeed.screen


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.konkuk.moru.R
import com.konkuk.moru.presentation.routinefeed.component.notification.NotificationRow
import com.konkuk.moru.presentation.routinefeed.component.topAppBar.BasicTopAppBar
import com.konkuk.moru.presentation.routinefeed.viewmodel.NotificationViewModel
import com.konkuk.moru.ui.theme.MORUTheme
import com.konkuk.moru.ui.theme.moruFontBold
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private val KST: ZoneId = ZoneId.of("Asia/Seoul") // 변경: KST 고정

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    onNavigateBack: () -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.items.isEmpty()) {
        if (state.items.isEmpty()) {
            viewModel.loadFirstPage(size = 20)
        }
    }

    // 상대/날짜 문자열을 섹션으로 그룹핑
    val grouped = remember(state.items) {
        state.items.groupBy { item ->
            item.createdAt?.let { bucketFromInstant(it) }
                ?: parseRelativeOrDateToBucket(item.relativeTime)
        }
    }

    Scaffold(
        modifier = Modifier.padding(bottom = 80.dp),
        containerColor = Color.White,
        topBar = {
            BasicTopAppBar(
                title = "알림",
                spacingBetweenIconAndTitle = 27.dp,
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "뒤로가기",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF1F3F5),
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                ),
                titleStyle = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = moruFontBold, fontSize = 16.sp, lineHeight = 28.sp
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            grouped.forEach { (sectionTitle, list) ->
                stickyHeader { SectionHeader(title = sectionTitle) }

                items(items = list, key = { it.id }) { item ->

                    val displayRelative = item.createdAt?.let { relativeFromInstant(it) }
                        ?: normalizeRelativeForDisplay(item.relativeTime)

                    // 스와이프 상태
                    val dismissState = rememberSwipeToDismissBoxState(
                        initialValue = SwipeToDismissBoxValue.Settled,
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.deleteNotification(item.id)
                                true
                            } else false
                        }
                    )

                    // 스와이프 컨테이너
                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = false, // 오른→왼 만
                        backgroundContent = {
                            // 뒤(배경): 빨간 삭제 영역
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFFF3B30))
                                    .padding(horizontal = 24.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "삭제",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "삭제",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    ) {
                        // 앞(내용): 네가 만든 셀. 평소에 빨강이 보이지 않도록 흰 배경으로 감싼다.
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White
                        ) {
                            NotificationRow(
                                senderId = item.senderId,
                                nickname = item.senderNickname,
                                profileUrl = item.senderProfileImage,
                                message = item.message,
                                relativeTime = displayRelative,
                                onProfileClick = { uid ->
                                    navController.navigate(
                                        com.konkuk.moru.presentation.navigation.Route.UserProfile
                                            .createRoute(uid)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            if (state.error != null) {
                item { Text("에러: ${state.error}", color = Color.Red) }
            }

            if (state.hasNext || state.isLoadingMore) {
                item {
                    TextButton(
                        onClick = { viewModel.loadAllRemaining(size = 20) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isLoadingMore
                    ) {
                        Text(
                            text = if (state.isLoadingMore) "모두 불러오는 중..." else "더보기",
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_down),
                            contentDescription = "아래화살표",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}


/* ===================== 시간 유틸 & 섹션 헤더 ===================== */

// API의 relativeTime 문자열로 “오늘/하루 전/N일 전/오래 전” 버킷을 만듭니다.
private fun parseRelativeToDayBucket(text: String): String {
    val cleaned = text.trim()
    // 흔한 표현들: "방금 전", "30초 전", "5분 전", "2시간 전", "어제", "하루 전", "2일 전", "3일 전", ...
    if (cleaned.contains("방금")) return "오늘"
    if (cleaned.contains("어제")) return "하루 전" // 혹시 서버가 "어제"로 주는 케이스
    if (cleaned.endsWith("초 전") || cleaned.endsWith("분 전") || cleaned.endsWith("시간 전")) return "오늘"

    val day = Regex("""(\d+)\s*일 전""").find(cleaned)?.groupValues?.getOrNull(1)?.toIntOrNull()
    return when (day) {
        null, 0 -> "오늘"
        1 -> "하루 전"
        2 -> "이틀 전"
        in 3..6 -> "${day}일 전"
        else -> "오래 전"
    }
}

private fun relativeFromInstant(instant: Instant, now: Instant = Instant.now()): String {
    val diff = Duration.between(instant, now)
    val minutes = diff.toMinutes()
    return when {
        minutes < 1 -> "방금 전"
        minutes < 60 -> "${minutes}분 전"
        minutes < 60 * 24 -> "${diff.toHours()}시간 전"
        minutes < 60 * 24 * 7 -> "${diff.toDays()}일 전"
        else -> DateTimeFormatter.ofPattern("yyyy.MM.dd")
            .format(ZonedDateTime.ofInstant(instant, KST))
    }
}

private fun bucketFromInstant(instant: Instant, today: LocalDate = LocalDate.now(KST)): String {
    val date = LocalDateTime.ofInstant(instant, KST).toLocalDate()
    val days = ChronoUnit.DAYS.between(date, today).toInt()
    return when {
        days == 0 -> "오늘"
        days in 1..7 -> "최근 7일"
        else -> "오래 전"
    }
}

// ✅ relativeTime 문자열이 "yyyy-MM-dd" 또는 ISO면 파싱해서 버킷 생성
private fun parseRelativeOrDateToBucket(text: String): String {
    parseAnyDateToInstant(text)?.let { return bucketFromInstant(it) }
    return parseRelativeToDayBucket(text) // 기존 로직(방금 전/분 전/시간 전/…)
}

// ✅ relativeTime 문자열이 날짜면 'n일 전/날짜'로 정규화해서 표시
private fun normalizeRelativeForDisplay(text: String): String {
    parseAnyDateToInstant(text)?.let { return relativeFromInstant(it) }
    return text // 이미 "2분 전" 등 상대문자면 그대로 사용
}

// ✅ 다양한 날짜 포맷 시도
private fun parseAnyDateToInstant(s: String): Instant? {
    val t = s.trim()
    // 1) ISO-8601 with offset/Z
    runCatching { return OffsetDateTime.parse(t).toInstant() }.onFailure { }
    // 2) yyyy-MM-dd
    runCatching {
        val d = LocalDate.parse(t, DateTimeFormatter.ISO_LOCAL_DATE)
        return d.atStartOfDay(KST).toInstant()
    }.onFailure { }
    // 3) yyyy.MM.dd
    runCatching {
        val d = LocalDate.parse(t, DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        return d.atStartOfDay(KST).toInstant()
    }.onFailure { }
    // 필요한 경우 yyyy/MM/dd 등 추가
    return null
}

// 섹션 헤더
@Composable
private fun SectionHeader(title: String) {
    Surface(
        color = Color.White,
        contentColor = Color.Black,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

/* ===================== 전체 프리뷰 (섹션 포함) ===================== */

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun NotificationScreenGroupedPreview() {
    MORUTheme {
        Surface {
            // API 응답 형태에 맞춘 더미 아이템
            data class Item(
                val id: String,
                val senderId: String,
                val senderNickname: String,
                val senderProfileImage: String?,
                val message: String,
                val relativeTime: String
            )

            val items = listOf(
                Item("1", "u1", "MORU", null, "MORU님이 ‘아침 조깅’ 루틴을 좋아합니다.", "5분 전"),
                Item("2", "u2", "Alice", null, "Alice님이 회원님을 팔로우하기 시작했습니다.", "2시간 전"),
                Item("3", "u3", "Bob", null, "Bob님이 ‘영어 공부’ 루틴을 스크랩했습니다.", "하루 전"),
                Item("4", "u4", "Chris", null, "Chris님이 ‘코딩’ 루틴을 좋아합니다.", "3일 전"),
                Item("5", "u5", "Dana", null, "Dana님이 ‘물마시기’ 루틴을 좋아합니다.", "30분 전")
            )

            val grouped = items.groupBy { parseRelativeToDayBucket(it.relativeTime) }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                grouped.forEach { (sectionTitle, list) ->
                    stickyHeader { SectionHeader(title = sectionTitle) }
                    items(list, key = { it.id }) { item ->
                        NotificationRow(
                            senderId = item.senderId,
                            nickname = item.senderNickname,
                            profileUrl = item.senderProfileImage,
                            message = item.message,
                            relativeTime = item.relativeTime,
                            onProfileClick = {}
                        )
                    }
                }
            }
        }
    }
}
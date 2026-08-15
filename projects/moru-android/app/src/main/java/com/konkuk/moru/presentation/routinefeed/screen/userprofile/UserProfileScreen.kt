package com.konkuk.moru.presentation.routinefeed.screen.userprofile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.konkuk.moru.R
import com.konkuk.moru.core.component.button.MoruButton
import com.konkuk.moru.core.component.routine.RoutineListItem
import com.konkuk.moru.data.model.Routine
import com.konkuk.moru.presentation.navigation.Route
import com.konkuk.moru.presentation.navigation.navigateUpOrHome
import com.konkuk.moru.presentation.routinefeed.component.topAppBar.BasicTopAppBar
import com.konkuk.moru.presentation.routinefeed.data.UserProfileUiState
import com.konkuk.moru.presentation.routinefeed.viewmodel.UserProfileViewModel
import com.konkuk.moru.ui.theme.MORUTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController,
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, uiState.userId) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val key = "follow_result_${uiState.userId}"
                val handle = navController.currentBackStackEntry?.savedStateHandle
                val saved = handle?.remove<Boolean>(key) // 있으면 꺼내고 지움
                if (saved != null) {
                    viewModel.applyExternalFollow(saved)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    Scaffold(
        containerColor = Color.White,
        topBar = {
            BasicTopAppBar(
                title = uiState.nickname.ifBlank { "사용자명" },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUpOrHome() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { paddingValues ->
        UserProfileContent(
            modifier = Modifier
                .padding(paddingValues)
                .padding(bottom = 80.dp)
                .background(Color.White),
            state = uiState,
            onFollowClick = viewModel::toggleFollow,
            onToggleExpansion = viewModel::toggleRunningRoutineExpansion,
            onLikeClick = viewModel::toggleLike,
            onFollowerClick = {
                navController.navigate(
                    Route.Follow.createRoute(
                        uiState.userId,
                        "follower"
                    )
                )
            },
            onFollowingClick = {
                navController.navigate(
                    Route.Follow.createRoute(
                        uiState.userId,
                        "following"
                    )
                )
            },
            onRoutineClick = { routineId ->
                navController.navigate(Route.RoutineFeedDetail.createRoute(routineId))
            }
        )
    }
}

@Composable
private fun UserProfileContent(
    modifier: Modifier = Modifier,
    state: UserProfileUiState,
    onFollowClick: () -> Unit,
    onToggleExpansion: () -> Unit,
    onLikeClick: (String) -> Unit,
    onFollowerClick: () -> Unit,
    onFollowingClick: () -> Unit,
    onRoutineClick: (String) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        item {
            ProfileHeader(
                state = state,
                onFollowClick = onFollowClick,
                onFollowerClick = onFollowerClick,
                onFollowingClick = onFollowingClick
            )
        }
        item {
            ExpandableRoutineSection(
                isExpanded = state.isRunningRoutineExpanded,
                routines = state.runningRoutines,
                nickname = state.nickname,
                onToggle = onToggleExpansion,
                onLikeClick = onLikeClick,
                onRoutineClick = onRoutineClick
            )
        }
        item {
            Text(
                text = "${state.nickname}님의 루틴",
                style = MORUTheme.typography.title_B_20,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 17.dp, top = 13.dp)
            )
        }
        if (state.userRoutines.isEmpty()) {
            item {
                EmptyRoutineView(modifier = Modifier.padding(vertical = 93.dp))
            }
        } else {
            items(state.userRoutines, key = { it.routineId }) { routine ->
                RoutineListItem(
                    isRunning = routine.isRunning,
                    routineName = routine.title,
                    tags = routine.tags,
                    likeCount = routine.likes,
                    isLiked = routine.isLiked,
                    onItemClick = { onRoutineClick(routine.routineId) }
                )
            }
        }
    }
}

// --- [수정] 생략되었던 세부 UI 컴포넌트들 모두 포함 ---

@Composable
private fun ProfileHeader(
    state: UserProfileUiState,
    onFollowClick: () -> Unit,
    onFollowerClick: () -> Unit,
    onFollowingClick: () -> Unit
) {
    val buttonText = if (state.isFollowing) "팔로잉" else "팔로우"
    val backgroundColor = if (state.isFollowing) MORUTheme.colors.veryLightGray else Color.Black
    val contentColor =
        if (state.isFollowing) MORUTheme.colors.mediumGray else MORUTheme.colors.limeGreen
    val showFollowButton = state.isMe == false   // null이면 숨김, false일 때만 노출
    val bottomRoundedCornerShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 8.dp,
        bottomEnd = 8.dp
    )



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp, // 그림자 깊이
                shape = bottomRoundedCornerShape, // 그림자 모양
                spotColor = Color.Black.copy(alpha = 0.10f) // 그림자 색상 (필요시)
            )
            .background(
                color = Color.White,
                shape = bottomRoundedCornerShape
            )
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = state.profileImageUrl, // UiState에서 URL을 가져옵니다.
                contentDescription = "프로필 사진",
                placeholder = painterResource(id = R.drawable.ic_profile_with_background), // 로딩 중에 보여줄 이미지
                error = painterResource(id = R.drawable.ic_profile_with_background), // 에러 시 보여줄 이미지
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(24.dp))
            Column(modifier = Modifier.weight(1f)) {

                AnimatedVisibility(
                    visible = showFollowButton,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column {
                        MoruButton(
                            text = if (state.isFollowing) "팔로잉" else "팔로우",
                            onClick = onFollowClick,
                            backgroundColor = if (state.isFollowing) MORUTheme.colors.veryLightGray else Color.Black,
                            contentColor = if (state.isFollowing) MORUTheme.colors.mediumGray else MORUTheme.colors.limeGreen,
                            shape = RoundedCornerShape(140.dp),
                            textStyle = MORUTheme.typography.title_B_14,
                            modifier = Modifier
                                .height(37.dp)
                                .width(88.dp),
                            enabled = !state.isFollowLoading
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
                ProfileStats(
                    routineCount = state.routineCount,
                    followerCount = state.followerCount,
                    followingCount = state.followingCount,
                    onFollowerClick = onFollowerClick,
                    onFollowingClick = onFollowingClick
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = state.nickname,
            style = MORUTheme.typography.time_R_16,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = state.bio,
            fontSize = 14.sp,
            style = MORUTheme.typography.time_R_14,
            color = Color.DarkGray
        )
    }
}

@Composable
private fun ProfileStats(
    routineCount: Int,
    followerCount: Int,
    followingCount: Int,
    onFollowerClick: () -> Unit,
    onFollowingClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatItem("루틴", routineCount)
        StatItem("팔로워", followerCount, onFollowerClick)
        StatItem("팔로잉", followingCount, onFollowingClick)
    }
}

@Composable
private fun StatItem(label: String, count: Int, onClick: (() -> Unit)? = null) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    ) {
        Text(text = count.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
private fun ExpandableRoutineSection(
    isExpanded: Boolean,
    routines: List<Routine>,
    nickname: String,
    onToggle: () -> Unit,
    onLikeClick: (String) -> Unit,
    onRoutineClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MORUTheme.colors.veryLightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "펼치기/접기",
                modifier = Modifier.clickable(onClick = onToggle)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$nickname 님의 실행 중인 루틴",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(onClick = onToggle)
            )
        }
        AnimatedVisibility(visible = isExpanded) {
            if (routines.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_person_standing),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        tint = Color.Unspecified
                    )
                    Text(
                        "현재 실행중인 루틴이 없습니다.",
                        color = MORUTheme.colors.darkGray,
                        style = MORUTheme.typography.desc_M_16
                    )
                }
            } else {
                Column {
                    routines.forEach { routine ->
                        RoutineListItem(
                            isRunning = true,//routine.isRunning,
                            routineName = routine.title,
                            tags = routine.tags,
                            likeCount = routine.likes,
                            isLiked = routine.isLiked,
                            onItemClick = { onRoutineClick(routine.routineId) }
                        )
                    }
                }
            }
        }
    }
}


// --- 프리뷰 ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserProfileScreenPreview(isDataEmpty: Boolean = false) {
    // [수정] 프리뷰용 샘플 데이터를 통합 Routine 모델로 변경
    val sampleRunningRoutines = remember {
        listOf(
            Routine(
                "routine-1",
                "아침 운동 1",
                "",
                null,
                "운동",
                listOf("#테그그그그그", "#tag"),
                "user-1",
                "모루",
                null,
                16,
                true,
                false,
                true
            )
        )
    }
    val sampleUserRoutines = remember {
        List(5) { index ->
            Routine(
                "user-routine-${index}",
                "아침 운동",
                "",
                null,
                "운동",
                listOf("#모닝루틴", "#스트레칭"),
                "user-2",
                "모루",
                null,
                16,
                false,
                index % 2 == 0,
                false
            )
        }
    }

    var isFollowing by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(true) }

    val likedStates = remember {
        val allRoutines = sampleRunningRoutines + sampleUserRoutines
        mutableStateMapOf(*allRoutines.map { it.routineId to it.isLiked }.toTypedArray())
    }
    val likeCounts = remember {
        val allRoutines = sampleRunningRoutines + sampleUserRoutines
        mutableStateMapOf(*allRoutines.map { it.routineId to it.likes }.toTypedArray())
    }

    val runningRoutines = sampleRunningRoutines.map {
        it.copy(
            isLiked = likedStates[it.routineId] ?: it.isLiked,
            likes = likeCounts[it.routineId] ?: it.likes
        )
    }
    val userRoutines = sampleUserRoutines.map {
        it.copy(
            isLiked = likedStates[it.routineId] ?: it.isLiked,
            likes = likeCounts[it.routineId] ?: it.likes
        )
    }

    val state = UserProfileUiState(
        nickname = "팔로우",
        bio = "자기소개입니다. 자기소개입니다.",
        profileImageUrl = "https://images.unsplash.com/photo-1580489944761-15a19d654956",
        routineCount = if (isDataEmpty) 0 else 4,
        followerCount = 628,
        followingCount = 221,
        isFollowing = isFollowing,
        isRunningRoutineExpanded = isExpanded,
        runningRoutines = if (isDataEmpty) emptyList() else runningRoutines,
        userRoutines = if (isDataEmpty) emptyList() else userRoutines,
    )

    MORUTheme {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                BasicTopAppBar(
                    title = state.nickname,
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "뒤로가기",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    },
                    spacingBetweenIconAndTitle = 24.dp,
                )
            }
        ) { paddingValues ->
            UserProfileContent(
                modifier = Modifier.padding(paddingValues),
                state = state,
                onFollowClick = { isFollowing = !isFollowing },
                onToggleExpansion = { isExpanded = !isExpanded },
                onLikeClick = { id ->
                    val currentStatus = likedStates[id] ?: false
                    likedStates[id] = !currentStatus
                    val currentCount = likeCounts[id] ?: 0
                    likeCounts[id] = if (!currentStatus) currentCount + 1 else currentCount - 1
                },
                onFollowerClick = {},
                onFollowingClick = {},
                onRoutineClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Default State")
@Composable
fun UserProfileScreenDefaultPreview() {
    UserProfileScreenPreview(isDataEmpty = false)
}

@Preview(showBackground = true, name = "Empty State")
@Composable
fun UserProfileScreenEmptyPreview() {
    UserProfileScreenPreview(isDataEmpty = true)
}
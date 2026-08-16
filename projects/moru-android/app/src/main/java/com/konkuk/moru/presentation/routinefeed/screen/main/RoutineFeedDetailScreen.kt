package com.konkuk.moru.presentation.routinefeed.screen.main

import RoutineDetailTopAppBar
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.konkuk.moru.core.datastore.SocialMemory
import com.konkuk.moru.data.model.DummyData
import com.konkuk.moru.data.model.Routine
import com.konkuk.moru.presentation.navigation.Route
import com.konkuk.moru.presentation.routinefeed.component.RoutineDetail.RoutineHeader
import com.konkuk.moru.presentation.routinefeed.component.RoutineDetail.RoutineStepSection
import com.konkuk.moru.presentation.routinefeed.component.RoutineDetail.SimilarRoutinesSection
import com.konkuk.moru.presentation.routinefeed.component.modale.CenteredInfoDialog
import com.konkuk.moru.presentation.routinefeed.viewmodel.RoutineDetailViewModel
import com.konkuk.moru.ui.theme.MORUTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDetailScreen(
    routineId: String,
    onBackClick: () -> Unit,
    navController: NavController,
    viewModel: RoutineDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // [추가] 이전 화면에서 넘긴 선택 루틴이 있으면 우선 세팅
    LaunchedEffect(routineId) {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<String>("selectedRoutineJson")
            ?.let { json ->
                runCatching { Gson().fromJson(json, Routine::class.java) }.getOrNull()
            }?.let { pre ->
                viewModel.setInitialRoutine(pre)
            }

        // ✅ 재사용 방지(깨끗하게 비우기)
        navController.previousBackStackEntry?.savedStateHandle?.remove<String>("selectedRoutineJson")

        // ✅ 서버 최신 상세로 덮어쓰기
        viewModel.loadRoutine(routineId)
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val routine = uiState.routine
    if (routine == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("루틴 정보를 불러오지 못했습니다.")
        }
        return
    }

    val topBarLikeCount = remember(routine.routineId, routine.likes) {
        SocialMemory.getRoutine(routine.routineId)?.likeCount ?: routine.likes
    }
    // [추가] 디버그 로그 (문제 추적 시 유용)
    LaunchedEffect(topBarLikeCount) {
        Log.d(
            "RoutineDetailTopAppBar",
            "likeCount passed=$topBarLikeCount (uiState=${routine.likes})"
        )
    }

    Scaffold(
        containerColor = Color.White,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    RoutineHeader(
                        routine = routine,
                        onProfileClick = { authorId ->
                            navController.navigate(Route.UserProfile.createRoute(authorId))
                        }
                    )
                }

                item {
                    RoutineStepSection(
                        modifier = Modifier.padding(16.dp),
                        routine = routine,
                        isAdding = uiState.isAddingToMine,
                        showAddButton = uiState.canBeAddedToMyRoutines,
                        onAddToMyRoutineClick = { viewModel.addToMyRoutines() }
                    )
                }

                item {
                    HorizontalDivider(
                        thickness = 8.dp,
                        color = MORUTheme.colors.veryLightGray
                    )
                }

                item {
                    SimilarRoutinesSection(
                        modifier = Modifier.padding(bottom = 16.dp),
                        routines = uiState.similarRoutines,
                        onRoutineClick = { clickedRoutineId ->
                            navController.navigate(
                                Route.RoutineFeedDetail.createRoute(
                                    clickedRoutineId
                                )
                            )
                        },
                    )
                }


            }

            if (uiState.showAddedDialog) {
                CenteredInfoDialog(
                    onDismissRequest = { viewModel.dismissAddedDialog() }
                ) {
                    Text(
                        text = "루틴이 추가되었습니다.\n실천 시간대를 설정해주세요!",
                        textAlign = TextAlign.Center,
                        color = Color(0xFFE0E0E0),
                        style = MORUTheme.typography.desc_M_14
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp) // TopAppBar 높이보다 약간 넉넉하게 설정
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.White.copy(alpha = 0.3f), Color.Transparent)
                        )
                    )
            )


            RoutineDetailTopAppBar(
                likeCount = topBarLikeCount,         // ✅ 항상 서버값
                isLiked = routine.isLiked,
                isBookmarked = routine.isBookmarked,
                onLikeClick = {
                    if (!uiState.isLiking && !uiState.isLoading) viewModel.toggleLikeSync()
                },
                onBookmarkClick = {
                    if (!uiState.isScrapping && !uiState.isLoading) viewModel.toggleScrapSync()
                },
                onBackClick = onBackClick,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    titleContentColor = Color.White
                )
            )
        }
    }

    LaunchedEffect(uiState.showAddedDialog) {
        if (uiState.showAddedDialog) {
            kotlinx.coroutines.delay(1500)
            viewModel.dismissAddedDialog()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RoutineDetailScreenPreview() {
    MORUTheme {
        // [수정] routineId를 전달하도록 Preview 수정
        RoutineDetailScreen(
            routineId = DummyData.feedRoutines.first().routineId,
            onBackClick = {},
            navController = rememberNavController(),
        )
    }
}
package com.konkuk.moru.presentation.myactivity.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Text
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import android.util.Log
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.konkuk.moru.R
import com.konkuk.moru.presentation.myactivity.component.RecordCard
import com.konkuk.moru.presentation.myactivity.viewmodel.MyActRecordUi
import com.konkuk.moru.presentation.myactivity.viewmodel.MyActRecordViewModel
import com.konkuk.moru.presentation.navigation.Route
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography
import com.konkuk.moru.ui.theme.MORUTheme.typography
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate

@Composable
fun ActRecordScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    vm: MyActRecordViewModel = hiltViewModel()
) {
    // 갤럭시 뒤로가기 버튼 처리
    BackHandler {
        Log.d("ActRecordScreen", "🔄 BackHandler 호출됨 - 내 활동으로 이동")
        navController.navigate(Route.MyActivity.route) {
            popUpTo(Route.ActRecord.route) { inclusive = true }
            launchSingleTop = true
        }
    }
    
    val todayList by vm.today.collectAsState()
    val recentList by vm.recent.collectAsState()
    val allList by vm.all.collectAsState()

    LaunchedEffect(Unit) {
        Log.d("ActRecordScreen", "🎬 ActRecordScreen 화면 로드됨")
        vm.loadToday()
        vm.loadRecent()
        vm.loadAllFirst()
    }

    // 완료된 루틴 데이터를 지속적으로 확인
    LaunchedEffect(navController.currentBackStackEntry?.savedStateHandle) {
        Log.d("ActRecordScreen", "🔍 savedStateHandle LaunchedEffect 실행됨")
        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
        Log.d("ActRecordScreen", "📦 savedStateHandle 존재: ${savedStateHandle != null}")
        
        val completedTitle = try {
            savedStateHandle?.get<String>("completedRoutineTitle")
        } catch (e: Exception) {
            Log.e("ActRecordScreen", "❌ completedTitle 파싱 실패: exception=${e.javaClass.simpleName}")
            null
        }
        val completedTime = try {
            savedStateHandle?.get<String>("completedRoutineTime")
        } catch (e: Exception) {
            Log.e("ActRecordScreen", "❌ completedTime 파싱 실패: exception=${e.javaClass.simpleName}")
            null
        }
                        val completedTagsRaw = try {
                    savedStateHandle?.get<ArrayList<String>>("completedRoutineTags")
                } catch (e: Exception) {
                    Log.e("ActRecordScreen", "❌ completedTagsRaw 파싱 실패: exception=${e.javaClass.simpleName}")
                    null
                }
                val completedTags: List<String> = completedTagsRaw?.toList() ?: emptyList()

                val completedStepNamesRaw = try {
                    savedStateHandle?.get<ArrayList<String>>("completedRoutineStepNames")
                } catch (e: Exception) {
                    Log.e("ActRecordScreen", "❌ completedStepNamesRaw 파싱 실패: exception=${e.javaClass.simpleName}")
                    null
                }
                val completedStepNames: List<String> = completedStepNamesRaw?.toList() ?: emptyList()

                val completedStepDurationsRaw = try {
                    savedStateHandle?.get<ArrayList<String>>("completedRoutineStepDurations")
                } catch (e: Exception) {
                    Log.e("ActRecordScreen", "❌ completedStepDurationsRaw 파싱 실패: exception=${e.javaClass.simpleName}")
                    null
                }
                val completedStepDurations: List<String> = completedStepDurationsRaw?.toList() ?: emptyList()

                val completedCategory = try {
                    savedStateHandle?.get<String>("completedRoutineCategory")
                } catch (e: Exception) {
                    Log.e("ActRecordScreen", "❌ completedCategory 파싱 실패: exception=${e.javaClass.simpleName}")
                    null
                }

                val completedStartTime = try {
                    savedStateHandle?.get<String>("completedRoutineStartTime")
                } catch (e: Exception) {
                    Log.e("ActRecordScreen", "❌ completedStartTime 파싱 실패: exception=${e.javaClass.simpleName}")
                    null
                }

                val completedEndTime = try {
                    savedStateHandle?.get<String>("completedRoutineEndTime")
                } catch (e: Exception) {
                    Log.e("ActRecordScreen", "❌ completedEndTime 파싱 실패: exception=${e.javaClass.simpleName}")
                    null
                }

                Log.d(
                    "ActRecordScreen",
                    "📊 파싱 완료: hasTitle=${completedTitle != null}, " +
                        "hasDuration=${completedTime != null}, tagCount=${completedTags.size}, " +
                        "stepCount=${completedStepNames.size}, " +
                        "stepDurationCount=${completedStepDurations.size}, " +
                        "hasCategory=${completedCategory != null}, " +
                        "hasStartTime=${completedStartTime != null}, " +
                        "hasEndTime=${completedEndTime != null}"
                )

                if (completedTitle != null && completedTime != null) {
                    Log.d(
                        "ActRecordScreen",
                        "✅ 완료된 루틴 데이터 수신: tagCount=${completedTags.size}, " +
                            "stepCount=${completedStepNames.size}"
                    )
                    vm.addCompletedRoutine(
                        completedTitle, 
                        completedTags, 
                        completedTime, 
                        completedStepNames, 
                        completedStepDurations,
                        completedCategory ?: "",
                        completedStartTime ?: "",
                        completedEndTime ?: ""
                    )
                    
                    // 루틴 추가 후 데이터를 다시 로드
                    vm.loadToday()
                    vm.loadRecent()
                    vm.loadAllFirst()

                    // 데이터 사용 후 제거
                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                        remove<String>("completedRoutineTitle")
                        remove<String>("completedRoutineTime")
                        remove<ArrayList<String>>("completedRoutineTags")
                        remove<ArrayList<String>>("completedRoutineStepNames")
                        remove<ArrayList<String>>("completedRoutineStepDurations")
                        remove<String>("completedRoutineCategory")
                        remove<String>("completedRoutineStartTime")
                        remove<String>("completedRoutineEndTime")
                    }
                    Log.d("ActRecordScreen", "🗑️ 데이터 제거 완료")
                } else {
                    Log.d("ActRecordScreen", "⚠️ 완료된 루틴 데이터가 없거나 불완전함")
                }
    }

    val today = remember { LocalDate.now() }
    val last7List by remember(allList, today) {
        derivedStateOf {
            allList.filter { (today.toEpochDay() - it.startedAt.toEpochDay()) in 1..6 }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // BackTitle 대신 커스텀 뒤로가기 버튼 사용
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(24.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_a),
                contentDescription = "Back Icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        Log.d("ActRecordScreen", "🔄 뒤로가기 버튼 클릭 - 내 활동으로 이동")
                        // 내 활동 화면으로 이동
                        navController.navigate(Route.MyActivity.route) {
                            // 현재 화면(내 기록)을 백스택에서 제거
                            popUpTo(Route.ActRecord.route) { inclusive = true }
                            // 내 활동 화면으로 이동
                            launchSingleTop = true
                        }
                    }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "내 기록", style = typography.body_SB_16)
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 90.dp),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(18.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "오늘",
                    color = colors.black,
                    style = typography.body_SB_14,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            items(
                items = todayList,
                key = { rec: MyActRecordUi -> "today-${rec.id}" }
            ) { rec ->
                val safe = URLEncoder.encode(rec.title, StandardCharsets.UTF_8.toString())
                RecordCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(90f / 139f),
                    title = rec.title,
                    tags = rec.tags,
                    completeFlag = rec.isComplete,
                    time = rec.durationSec.toHms(),
                    onClick = { navController.navigate(Route.ActRecordDetail.createRoute(rec.id)) }
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "최근 7일",
                    color = colors.black,
                    style = typography.body_SB_14,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            items(items = recentList, key = { rec: MyActRecordUi -> "recent-${rec.id}" }) { rec ->
                val safe = URLEncoder.encode(rec.title, StandardCharsets.UTF_8.toString())
                RecordCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(90f / 139f),
                    title = rec.title,
                    tags = rec.tags,
                    completeFlag = rec.isComplete,
                    time = rec.durationSec.toHms(),
                    onClick = { navController.navigate(Route.ActRecordDetail.createRoute(rec.id)) }
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "전체",
                    color = colors.black,
                    style = typography.body_SB_14,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            itemsIndexed(allList, key = { idx, rec -> "all-${rec.id}-$idx" }) { index, rec ->
                if (index >= allList.lastIndex - 4) {
                    vm.loadAllNext()
                }
                val safe = URLEncoder.encode(rec.title, StandardCharsets.UTF_8.toString())
                RecordCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(90f / 139f),
                    title = rec.title,
                    tags = rec.tags,
                    completeFlag = rec.isComplete,
                    time = rec.durationSec.toHms(),
                    onClick = { navController.navigate(Route.ActRecordDetail.createRoute(rec.id)) }
                )
            }
        }
    }
}

private fun Long.toHms(): String {
    val h = this / 3600
    val m = (this % 3600) / 60
    val s = this % 60
    return "%02d:%02d:%02d".format(h, m, s)
}

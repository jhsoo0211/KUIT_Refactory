package com.konkuk.moru.presentation.myactivity.screen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.konkuk.moru.presentation.myactivity.component.BackTitle
import com.konkuk.moru.presentation.myactivity.component.ScrabRoutine
import com.konkuk.moru.presentation.myactivity.viewmodel.MyActScrapViewModel
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.ui.unit.sp
import com.konkuk.moru.presentation.navigation.Route

@Composable
fun ActScrabScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    vm: MyActScrapViewModel = hiltViewModel()
) {
    val TAG = "MyActScrabScreen"

    val scraps by vm.items.collectAsState()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    val selectedIndex = remember { mutableStateOf<Int?>(null) }
    val gridState = rememberLazyGridState()

    // 최초 로드
    LaunchedEffect(Unit) {
        Log.d(TAG, "screen start ⇒ loadFirst()")
        vm.loadFirst(size = 21)
    }

    // 스크롤 끝 근처에서 페이징
    LaunchedEffect(gridState, scraps.size, loading) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collectLatest { last ->
                if (last != null && !loading && scraps.isNotEmpty() && last >= scraps.lastIndex - 6) {
                    Log.d(TAG, "near end (last=$last), call loadNext()")
                    vm.loadNext(size = 21)
                }
            }
    }

    // 수신 데이터/상태 로그
    LaunchedEffect(scraps.size) {
        Log.d(TAG, "items size = ${scraps.size}")
    }
    LaunchedEffect(loading) { Log.d(TAG, "loading = $loading") }
    LaunchedEffect(error) {
        if (error != null) Log.e(TAG, "load failed")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            BackTitle(title = "스크랩한 루틴", navController = navController)
        }
        Spacer(modifier = Modifier.height(38.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 98.36.dp),
                modifier = Modifier.fillMaxWidth(),
                state = gridState,
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                contentPadding = PaddingValues()
            ) {
                // 빈 상태 표시
                if (!loading && scraps.isEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = "스크랩한 루틴이 없습니다.",
                            style = typography.desc_M_16.copy(fontSize = 14.sp),
                            color = colors.lightGray
                        )
                    }
                }

                itemsIndexed(scraps, key = { _, it -> it.routineId }) { index, scrap ->
                    ScrabRoutine(
                        imageUrl = scrap.imageUrl.takeIf { it.isNotBlank() },
                        title = scrap.title.ifBlank { "루틴명" },
                        tags = scrap.tagNames.map { "#$it" },
                        isSelected = selectedIndex.value == index,
                        onLongClick = {
                            selectedIndex.value = if (selectedIndex.value == index) null else index
                            Log.d(TAG, "longClick index=$index, selected=${selectedIndex.value}")
                        },
                        onClick = {
                            navController.navigate(Route.RoutineFeedDetail1.withId(scrap.routineId))
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(27.86.dp))
        if (selectedIndex.value != null) {
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .background(colors.charcoalBlack)
                    .height(80.dp)
                    .clickable { selectedIndex.value = null },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "추가하기",
                    color = colors.paleLime,
                    style = typography.desc_M_16
                )
            }
        }
    }
}

package com.konkuk.moru.presentation.routinefeed.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.core.component.routine.RoutineListItem
import com.konkuk.moru.data.model.DummyData
import com.konkuk.moru.data.model.Routine
import com.konkuk.moru.presentation.routinefeed.component.topAppBar.BasicTopAppBar
import com.konkuk.moru.ui.theme.MORUTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineFeedRec(
    title: String,
    routines: List<Routine>,
    onBack: () -> Unit,
    onRoutineClick: (String) -> Unit
) {
    var likeStates by remember(routines) {
        mutableStateOf(routines.associate { it.routineId to it.isLiked })
    }
    var likeCounts by remember(routines) {
        mutableStateOf(routines.associate { it.routineId to it.likes })
    }

    Scaffold(
        topBar = {
            BasicTopAppBar(
                title = title,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "뒤로가기",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF212120),
                    titleContentColor = MORUTheme.colors.limeGreen,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                titleStyle = MORUTheme.typography.body_SB_16
            )
        }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .padding(inner)
                .background(Color.White)
                .fillMaxSize(),
            contentPadding = PaddingValues(top = 18.dp, bottom = 80.dp)
        ) {
            items(routines) { r ->
                val liked = likeStates[r.routineId] ?: false
                val currentLikeCount = likeCounts[r.routineId] ?: r.likes

                RoutineListItem(
                    isRunning = r.isRunning,
                    routineName = r.title,
                    tags = r.tags,
                    likeCount = currentLikeCount,
                    isLiked = liked,
                    showCheckbox = false,
                    onItemClick = { onRoutineClick(r.routineId) }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun HotPreview() {
    MORUTheme {
        RoutineFeedRec(
            title = "지금 가장 핫한 루틴은?",
            routines = DummyData.feedRoutines,
            onBack = {},
            onRoutineClick = {}
        )
    }
}
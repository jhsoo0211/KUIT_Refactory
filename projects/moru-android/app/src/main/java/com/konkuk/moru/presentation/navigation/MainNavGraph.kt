package com.konkuk.moru.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.konkuk.moru.presentation.home.screen.HomeScreen
import com.konkuk.moru.presentation.home.screen.RoutineFocusIntroScreen
import com.konkuk.moru.presentation.home.screen.RoutineSimpleRunScreen
import com.konkuk.moru.presentation.myactivity.screen.ActFabTagScreen
import com.konkuk.moru.presentation.myactivity.screen.ActInsightInfoClickScreen
import com.konkuk.moru.presentation.myactivity.screen.ActMainScreen
import com.konkuk.moru.presentation.myactivity.screen.ActPrivacyPolicyScreen
import com.konkuk.moru.presentation.myactivity.screen.ActProfileScreen
import com.konkuk.moru.presentation.myactivity.screen.ActRecordDetailScreen
import com.konkuk.moru.presentation.myactivity.screen.ActRecordScreen
import com.konkuk.moru.presentation.myactivity.screen.ActScrabScreen
import com.konkuk.moru.presentation.myactivity.screen.ActSettingScreen
import com.konkuk.moru.presentation.myroutines.screen.MyRoutineDetailScreen
import com.konkuk.moru.presentation.myroutines.screen.MyRoutinesScreen
import com.konkuk.moru.presentation.myroutines.viewmodel.MyRoutinesViewModel
import com.konkuk.moru.presentation.routinecreate.screen.RoutineCreateScreen
import com.konkuk.moru.presentation.routinefeed.screen.NotificationScreen
import com.konkuk.moru.presentation.routinefeed.screen.follow.FollowScreen
import com.konkuk.moru.presentation.routinefeed.screen.main.RoutineDetailScreen
import com.konkuk.moru.presentation.routinefeed.screen.main.RoutineFeedRec
import com.konkuk.moru.presentation.routinefeed.screen.main.RoutineFeedScreen
import com.konkuk.moru.presentation.routinefeed.screen.search.TagSearchScreen
import com.konkuk.moru.presentation.routinefeed.screen.userprofile.UserProfileScreen
import com.konkuk.moru.presentation.routinefeed.viewmodel.RoutineFeedViewModel
import com.konkuk.moru.presentation.routinefocus.screen.RoutineFocusScreenContainer
import com.konkuk.moru.presentation.routinefocus.viewmodel.RoutineFocusViewModel
import com.konkuk.moru.presentation.routinefocus.viewmodel.SharedRoutineViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    innerPadding: PaddingValues = PaddingValues(),
    fabOffsetY: MutableState<Float>,
    todayTabOffsetY: MutableState<Float>,
    onShowOnboarding: () -> Unit,
    routineFocusViewModel: RoutineFocusViewModel? = null,
) {

    NavHost(
        navController = navController,
        startDestination = Route.Home.route
    ) {
        composable(route = Route.Home.route) { homeEntry ->
            val sharedViewModel: SharedRoutineViewModel = viewModel()
            HomeScreen(
                navController = navController,
                homeEntry = homeEntry,
                sharedViewModel = sharedViewModel,
                modifier = Modifier.padding(innerPadding),
                fabOffsetY = fabOffsetY,
                todayTabOffsetY = todayTabOffsetY,
                onShowOnboarding = onShowOnboarding,
            )
        }

//        // 루틴 목록의 카드 클릭 시
//        composable(
//            route = "routine_focus_intro/{routineId}",
//            arguments = listOf(navArgument("routineId") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val parent = remember(navController.currentBackStackEntry) {
//                navController.getBackStackEntry(Route.Home.route)
//            }
//            val shared = viewModel<SharedRoutineViewModel>(parent)
//
//            val rid = backStackEntry.arguments?.getInt("routineId")
//            if (rid != null) {
//                shared.setSelectedRoutineId(rid)
//            }
//
//            RoutineFocusIntroScreen(
//                sharedViewModel = shared,
//                onStartClick = { selectedSteps, title, hashTag ->
//                    shared.setSelectedSteps(selectedSteps)
//                    shared.setRoutineTitle(title)
//                    shared.setRoutineTags(hashTag.split(" ").map { it.removePrefix("#") })
//                    navController.navigate("routine_focus")
//                },
//                onBackClick = { navController.popBackStack() }
//            )
//        }


        composable(route = Route.RoutineFocusIntro.route) {

            val parentEntry = remember(navController.currentBackStackEntry) {
                navController.getBackStackEntry(Route.Home.route)
            }
            val sharedViewModel = viewModel<SharedRoutineViewModel>(parentEntry)

            val startNavigation by sharedViewModel.startNavigation.collectAsState()
            val category by sharedViewModel.routineCategory.collectAsState()

            RoutineFocusIntroScreen(
                sharedViewModel = sharedViewModel,
                onStartClick = { selectedSteps, title, hashTag, category, totalDuration ->
                    // 이미지 URL은 SharedRoutineViewModel에서 가져오기
                    val imageUrl = sharedViewModel.routineImageUrl.value

                    // 루틴 데이터 설정
                    sharedViewModel.setSelectedSteps(selectedSteps)
                    sharedViewModel.setRoutineTitle(title)
                    sharedViewModel.setRoutineTags(hashTag.split(" ").map { it.removePrefix("#") })
                    sharedViewModel.setRoutineCategory(category)
                    sharedViewModel.setTotalDuration(totalDuration)
                    sharedViewModel.setRoutineImageUrl(imageUrl)

                    // 실행 화면 이동
                    if (category == "집중") {
                        navController.navigate(Route.RoutineFocus.route)
                    } else {
                        navController.navigate(Route.RoutineSimpleRun.route)
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )

            LaunchedEffect(startNavigation) {
                when (startNavigation) {
                    "집중" -> {
                        navController.navigate(Route.RoutineFocus.route)
                        sharedViewModel.onNavigationHandled()
                    }

                    "간편" -> {
                        navController.navigate(Route.RoutineSimpleRun.route)
                        sharedViewModel.onNavigationHandled()
                    }

                    else -> Unit
                }
            }
        }



        composable(route = Route.RoutineSimpleRun.route) {
            val parent = remember(navController.currentBackStackEntry) {
                navController.getBackStackEntry(Route.Home.route)
            }
            val shared = viewModel<SharedRoutineViewModel>(parent)
            val currentId by shared.selectedRoutineId.collectAsState()
            val originalRoutineId by shared.originalRoutineId.collectAsState()
            val title by shared.routineTitle.collectAsState()
            val category by shared.routineCategory.collectAsState()
            val totalDuration by shared.totalDuration.collectAsState()
            val steps by shared.selectedSteps.collectAsState()


            if (currentId != null) {
                RoutineSimpleRunScreen(
                    sharedViewModel = shared,
                    routineId = currentId!!,
                    onDismiss = {
                        // 홈으로 돌아갈 때 "진행중 루틴" 알림

                        // originalRoutineId를 stableIntId로 변환해서 설정
                        val stableId = originalRoutineId?.toStableIntId()

                        navController.getBackStackEntry(Route.Home.route)
                            .savedStateHandle["runningRoutineId"] = stableId

                        // 간편 루틴은 실천율에 반영되지만 내 기록에는 표시되지 않음

                        navController.popBackStack(
                            Route.Home.route,
                            inclusive = false
                        )
                        if (navController.currentDestination?.route != Route.Home.route) {
                            navController.navigate(Route.Home.route) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                launchSingleTop = true
                            }
                        }

                    },
                    onFinishConfirmed = { finishedId: String ->

                        // 간편 루틴 완료 시 실천율 업데이트 (RoutineSimpleRunScreen에서 처리됨)
                        // 내 기록에는 표시되지 않음

                        navController.getBackStackEntry(Route.Home.route)
                            .savedStateHandle["finishedRoutineId"] = originalRoutineId ?: finishedId
                        navController.popBackStack(Route.Home.route, false)
                    }
                )
            }
        }


        composable(route = Route.RoutineFocus.route) {
            // 전달받은 RoutineFocusViewModel 사용
            val routineFocusViewModel: RoutineFocusViewModel = routineFocusViewModel ?: viewModel()

            // Home NavGraph의 ViewModel을 공유
            val parentEntry = remember(navController.currentBackStackEntry) {
                navController.getBackStackEntry(Route.Home.route)
            }
            val sharedViewModel = viewModel<SharedRoutineViewModel>(parentEntry)
            val title by sharedViewModel.routineTitle.collectAsState()
            val category by sharedViewModel.routineCategory.collectAsState()
            val totalDuration by sharedViewModel.totalDuration.collectAsState()
            val steps by sharedViewModel.selectedSteps.collectAsState()
            val originalRoutineId by sharedViewModel.originalRoutineId.collectAsState()
            val selectedApps by sharedViewModel.selectedApps.collectAsState()


            // 집중 루틴 화면 진입 시 선택된 앱들을 설정 (타이머는 시작하지 않음)
            LaunchedEffect(selectedApps) {
                // 새로운 루틴 시작 전 완전한 초기화
                routineFocusViewModel.endFocusRoutine()
                
                routineFocusViewModel.setSelectedApps(selectedApps)
                // 타이머는 startFocusRoutine() 호출하지 않음 - intro 화면에서 시작됨
            }

            RoutineFocusScreenContainer(
                focusViewModel = routineFocusViewModel,
                sharedViewModel = sharedViewModel,
                onDismiss = {
                    routineFocusViewModel.endFocusRoutine()
                    navController.popBackStack(
                        Route.Home.route,
                        inclusive = false
                    )
                    if (navController.currentDestination?.route != Route.Home.route) {
                        navController.navigate(Route.Home.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                onFinishConfirmed = { finishedId: String ->
                    routineFocusViewModel.endFocusRoutine()
                    
                    // 간편 루틴과 동일한 방식으로 총 소요시간 설정
                    val totalElapsedSeconds = routineFocusViewModel.totalElapsedSeconds + routineFocusViewModel.elapsedSeconds
                    sharedViewModel.setTotalDuration(totalElapsedSeconds)
                    
                    navController.getBackStackEntry(Route.Home.route)
                        .savedStateHandle["finishedRoutineId"] = originalRoutineId ?: finishedId
                    navController.popBackStack(Route.Home.route, false)
                },
                onNavigateToMyActivity = {
                    // 완료된 루틴 데이터를 ActRecord 화면으로 전달
                    val title = sharedViewModel.routineTitle.value
                    val tags = sharedViewModel.routineTags.value
                    val totalDuration = sharedViewModel.totalDuration.value

                    // 데이터를 savedStateHandle에 저장
                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                        set("completedRoutineTitle", title)
                        set("completedRoutineTime", totalDuration)
                        set("completedRoutineTags", tags)
                    }

                    // 집중화면을 백스택에서 제거하고 내 활동 화면으로 이동
                    navController.navigate(Route.ActRecord.route) {
                        // 집중화면을 백스택에서 제거
                        popUpTo(Route.RoutineFocus.route) { inclusive = true }
                        // 내 활동 화면으로 이동
                        launchSingleTop = true
                    }
                }
            )
        }


        composable(route = Route.RoutineFeed.route) {
            val viewModel: RoutineFeedViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            RoutineFeedScreen(
                navController = navController,
            )
        }

        composable(route = Route.RoutineSearch.route) {
            // 변경: SearchViewModel 주입
            val vm: com.konkuk.moru.presentation.routinefeed.viewmodel.SearchViewModel =
                androidx.hilt.navigation.compose.hiltViewModel()

            // 변경: Host에 vm 전달
            com.konkuk.moru.presentation.routinefeed.screen.search.RoutineSearchHost(
                navController = navController
            )
        }

        /*composable(
            route = Route.TagSearch.route,
            arguments = listOf(
                navArgument("originalQuery") {
                    type = NavType.StringType
                    defaultValue = "" // 없으면 빈 문자열
                }
            )
        ) { backStackEntry ->
            val originalQuery = backStackEntry.arguments?.getString("originalQuery") ?: ""

            // ⚠️ 기존에 사용 중인 TagSearch 화면을 그대로 호출
            com.konkuk.moru.presentation.routinefeed.screen.search.TagSearchScreen(
                originalQuery = originalQuery,
                onNavigateBack = { navController.popBackStack() },
                onTagSelected = { tagName ->
                    // 선택 결과를 이전 스택(MyRoutineDetail)으로 돌려준다
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("picked_tag_name", tagName) // 이름만 전달
                    navController.popBackStack() // 검색 화면 닫기
                }
            )
        }*/

        composable(
            route = "${Route.TagSearch.route}?originalQuery={originalQuery}",
            arguments = listOf(
                navArgument("originalQuery") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val originalQuery = backStackEntry.arguments?.getString("originalQuery").orEmpty()
            TagSearchScreen(
                originalQuery = originalQuery,
                onNavigateBack = { navController.popBackStack() },
                onTagSelected = { nameOrHash ->
                    val normalized = nameOrHash.removePrefix("#")
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selectedTagsResult", listOf(normalized))
                    navController.popBackStack()
                }
            )
        }


        composable(
            route = Route.RoutineFeedDetail.route,
            arguments = listOf(navArgument("routineId") { type = NavType.StringType })
        ) { backStackEntry ->
            val routineId = backStackEntry.arguments?.getString("routineId")
            if (routineId != null) {
                RoutineDetailScreen(
                    routineId = routineId,
                    onBackClick = { navController.navigateUpOrHome() },
                    navController = navController
                )
            } else {
                navController.popBackStack()
            }
        }


        composable(
            route = Route.RoutineFeedRec.route,
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedTitle = backStackEntry.arguments?.getString("title") ?: ""
            val title = URLDecoder.decode(encodedTitle, StandardCharsets.UTF_8.toString())

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Route.RoutineFeed.route)
            }
            val feedViewModel: RoutineFeedViewModel = hiltViewModel(parentEntry) // ✅ [추가]
            val uiState by feedViewModel.uiState.collectAsState()

            val routinesToShow = remember(uiState, title) {
                uiState.routineSections.firstOrNull { it.title == title }?.routines.orEmpty()
            }



            RoutineFeedRec(
                title = title,
                routines = routinesToShow,
                onBack = { navController.navigateUpOrHome() },
                onRoutineClick = { routineId ->
                    routinesToShow.firstOrNull { it.routineId == routineId }?.let { selected ->
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("selectedRoutineJson", Gson().toJson(selected))
                    }
                    navController.navigate(Route.RoutineFeedDetail.createRoute(routineId))
                }
            )
        }

        composable(route = Route.MyRoutine.route) {
            val viewModel: MyRoutinesViewModel = hiltViewModel()

            MyRoutinesScreen(
                viewModel = viewModel,
                onNavigateToRoutineFeed = { navController.navigate(Route.RoutineFeed.route) },
                onNavigateToDetail = { routineId ->
                    val target = Route.MyRoutineDetail.createRoute(routineId)
                    navController.navigate(target)
                },
                onNavigateToCreateRoutine = { navController.navigate(Route.RoutineCreate.route) }
            )
        }

        // [추가] UserProfileScreen 내비게이션 설정
        composable(
            route = Route.MyRoutineDetail.route,
            arguments = listOf(navArgument(Route.MyRoutineDetail.KEY) { type = NavType.StringType })
        ) { backStackEntry ->
            val routineId = backStackEntry.arguments?.getString(Route.MyRoutineDetail.KEY)
            if (routineId != null) {
                MyRoutineDetailScreen(
                    routineId = routineId,
                    onBackClick = { navController.popBackStack() },
                    navController = navController
                )
            } else {
                navController.popBackStack()
            }
        }


        composable(
            route = Route.UserProfile.route,
            arguments = listOf(navArgument("userId") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            // userId는 현재 더미 데이터로만 사용되므로 ViewModel에서 직접 로드합니다.
            // 실제 앱에서는 hiltViewModel에 userId를 전달하여 해당 유저 데이터를 불러옵니다.
            UserProfileScreen(navController = navController)
        }

        // [추가] FollowScreen 내비게이션 설정
        composable(
            route = Route.Follow.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("selectedTab") { type = NavType.StringType })
        ) { backStackEntry ->
            val selectedTab = backStackEntry.arguments?.getString("selectedTab")
            FollowScreen(
                onBackClick = { navController.navigateUpOrHome() },
                selectedTab = selectedTab,
                onUserClick = { userId ->
                    navController.navigate(Route.UserProfile.createRoute(userId))
                },
                navController = navController
            )
        }

        composable(route = Route.MyActivity.route) {
            ActMainScreen(
                navController = navController
            )
        }

        composable(route = Route.ActSetting.route) {
            ActSettingScreen(
                navController = navController
            )
        }

        composable(route = Route.Notification.route) {
            NotificationScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                navController = navController
            )
        }

        composable(route = Route.ActScrab.route) {
            ActScrabScreen(
                navController = navController
            )
        }


        composable(route = Route.ActFabTag.route) {
            ActFabTagScreen(
                navController = navController
            )
        }

        composable(route = Route.ActRecord.route) {
            ActRecordScreen(
                navController = navController
            )
        }

        composable(route = Route.ActProfile.route) {
            ActProfileScreen(
                navController = navController
            )
        }

        composable(
            route = Route.ActRecordDetail.route,
            arguments = listOf(navArgument("logId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val logId = backStackEntry.arguments?.getString("logId") ?: ""

            ActRecordDetailScreen(
                logId = logId,
                navController = navController
            )
        }

        composable(route = Route.ActInsightInfo.route) {
            ActInsightInfoClickScreen(
                navController = navController
            )
        }

        // 루틴 생성 화면
        composable(route = Route.RoutineCreate.route) {
            RoutineCreateScreen(navController)
        }

        composable(route = Route.ActPolicy.route) {
            ActPrivacyPolicyScreen(navController)
        }

        composable(
            route = Route.RoutineFeedDetail1.pattern,
            arguments = listOf(navArgument(Route.RoutineFeedDetail1.arg) { type = NavType.StringType })
        ) { backStackEntry ->
            val routineId = backStackEntry.arguments?.getString(Route.RoutineFeedDetail1.arg)
            if (routineId != null) {
                RoutineDetailScreen(
                    routineId = routineId,
                    onBackClick = { navController.navigateUpOrHome() },
                    navController = navController
                )
            } else {
                navController.popBackStack()
            }
        }
    }
}

// String ID → 안정적인 Int 키 (기존 Int API/콜백용)
private fun String.toStableIntId(): Int {
    this.toLongOrNull()?.let {
        val mod = (it % Int.MAX_VALUE).toInt()
        return if (mod >= 0) mod else -mod
    }
    var h = 0
    for (ch in this) h = (h * 31) + ch.code
    return h
}

package com.konkuk.moru

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.konkuk.moru.core.datastore.LoginPreference
import com.konkuk.moru.core.datastore.OnboardingPreference
import com.konkuk.moru.presentation.navigation.AppNavGraph
import com.konkuk.moru.presentation.navigation.Route
import com.konkuk.moru.presentation.routinefocus.viewmodel.RoutineFocusViewModel
import com.konkuk.moru.ui.theme.MORUTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val focusViewModel: RoutineFocusViewModel by viewModels()
    private var navController: androidx.navigation.NavHostController? = null

    // --- FCM 알림 권한 요청 로직 추가 ---
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _: Boolean -> }

    private fun askNotificationPermission() {
        // 이 코드는 Android 13 (TIRAMISU) 이상에서만 실행됩니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                // 권한 요청
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    // ------------------------------------

        private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                
                // 현재 화면 경로 확인
                val currentRoute = navController?.currentDestination?.route

                // 집중 루틴 화면에서만 화면 차단 팝업 표시
                if (focusViewModel.isFocusRoutineActive && currentRoute == "routine_focus") {
                    focusViewModel.showScreenBlockPopup(focusViewModel.selectedApps)
                } else {
                    // 다른 화면이거나 집중 루틴이 아닌 경우 기본 뒤로가기 동작
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        // 현재 화면 경로 확인
        val currentRoute = navController?.currentDestination?.route

        // 집중 루틴 화면에서만 화면 차단 처리
        if (focusViewModel.isFocusRoutineActive && currentRoute == "routine_focus") {
            when (keyCode) {
                KeyEvent.KEYCODE_MENU -> {
                    focusViewModel.showScreenBlockOverlay(focusViewModel.selectedApps)
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        super.onPause()
        
        // 현재 화면 경로 확인
        val currentRoute = navController?.currentDestination?.route
        
        // 집중 루틴 화면에서만 화면 차단 처리
        if (focusViewModel.isFocusRoutineActive && currentRoute == "routine_focus" && !focusViewModel.isPermittedAppLaunch) {
            focusViewModel.showScreenBlockPopup(focusViewModel.selectedApps)
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        
        // 현재 화면 경로 확인
        val currentRoute = navController?.currentDestination?.route
        
        // 집중 루틴 화면에서만 화면 차단 처리
        if (focusViewModel.isFocusRoutineActive && currentRoute == "routine_focus") {
            focusViewModel.showScreenBlockOverlay(focusViewModel.selectedApps)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        
        // 현재 화면 경로 확인
        val currentRoute = navController?.currentDestination?.route
        
        // 집중 루틴 화면에서만 화면 차단 처리
        if (!hasFocus && focusViewModel.isFocusRoutineActive && currentRoute == "routine_focus") {
            focusViewModel.showScreenBlockOverlay(focusViewModel.selectedApps)
        }
    }

    // ✅✅✅ 1. 오류 수정: Intent?를 Intent로 변경 ✅✅✅
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
        setupBackPressHandler()

        setContent {
            MORUTheme {
                val navController = rememberNavController()
                this@MainActivity.navController = navController
                val context = applicationContext
                val isLoggedInState = remember { mutableStateOf(false) }
                var isOnboardingComplete by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    isOnboardingComplete = OnboardingPreference
                        .isOnboardingComplete(applicationContext)
                        .first()
                    isLoggedInState.value = LoginPreference.isLoggedIn(context).first()
                }

                AppNavGraph(
                    navController = navController,
                    routineFocusViewModel = focusViewModel
                )

                // ✅✅✅ 2. 알림 클릭 시 전달된 데이터 처리 로직 추가 ✅✅✅
                // LaunchedEffect의 key를 intent로 설정하여 새 인텐트가 들어올 때마다 이 블록이 다시 실행되도록 합니다.
                LaunchedEffect(key1 = intent) {
                    val routineId = intent.getStringExtra("ROUTINE_ID")
                    if (!routineId.isNullOrEmpty()) {
                        navController.navigate(Route.RoutineFeedDetail.createRoute(routineId))

                        // ✅ 처리가 끝난 인텐트의 데이터를 지워 중복 실행을 방지합니다.
                        intent.removeExtra("ROUTINE_ID")
                    }
                }

                LaunchedEffect(focusViewModel.isLandscapeMode) {
                    val newOrientation = if (focusViewModel.isLandscapeMode) {
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    } else {
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    }
                    if (requestedOrientation != newOrientation) {
                        requestedOrientation = newOrientation
                    }
                }
            }
        }
    }
}

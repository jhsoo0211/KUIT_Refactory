package com.konkuk.moru.presentation.myroutines.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.konkuk.moru.R
import com.konkuk.moru.core.component.routinedetail.DraggableAppSearchBottomSheet
import com.konkuk.moru.presentation.myroutines.viewmodel.MyRoutineDetailViewModel
import com.konkuk.moru.presentation.routinefeed.component.topAppBar.BasicTopAppBar
import com.konkuk.moru.ui.theme.MORUTheme
import androidx.activity.compose.BackHandler
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import java.io.File
import com.konkuk.moru.core.component.ImageChoiceOptionButtonScreen
import com.konkuk.moru.presentation.myroutines.component.MyRoutineDetailContent
import com.konkuk.moru.presentation.navigation.Route
import kotlinx.coroutines.flow.collectLatest
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.compose.runtime.key
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRoutineDetailScreen(
    routineId: String,
    onBackClick: () -> Unit,
    viewModel: MyRoutineDetailViewModel = hiltViewModel(),
    navController: NavHostController
) {
    // ✨ ViewModel의 UiState를 구독하여 단일 진실 공급원 원칙을 따름
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    var isBottomSheetOpen by remember { mutableStateOf(false) }
    val allApps by viewModel.availableApps.collectAsStateWithLifecycle()
    val selectedAppList = uiState.routine?.usedApps ?: emptyList()

    var isImageOptionVisible by remember { mutableStateOf(false) }
    val selectedImageUri by viewModel.localImageUri.collectAsStateWithLifecycle()

    // [변경] 카메라 촬영 준비 상태
    val cameraImageUriState = remember { mutableStateOf<Uri?>(null) }

    // [변경] 카메라 촬영 런처
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.updateLocalImage(cameraImageUriState.value) // ★ 여기
        }
    }

    // [변경] 카메라 권한 런처
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createImageUri(context)
            cameraImageUriState.value = uri
            takePictureLauncher.launch(uri)
        } else {
            // 권한 거부 시 안내 필요하면 토스트/스낵바 처리
        }
    }

    // [변경] 갤러리 선택 런처
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.updateLocalImage(uri) // ★ 여기
    }


    LaunchedEffect(routineId) {
        viewModel.loadRoutine(routineId)
    }
    LaunchedEffect(Unit) {
        viewModel.loadInstalledApps(context)
    }

    BackHandler(enabled = uiState.isEditMode) {
        viewModel.cancelEdits()
    }

    LaunchedEffect(Unit) {
        viewModel.deleteCompleted.collect {
            onBackClick()
        }
    }

    // (LaunchedEffect) 검색 결과 수신
    // ✅ 결과 수신: 내비 백스택 복귀 시점에 확실하게 받는다
    LaunchedEffect(navController) {
        val handle = navController.currentBackStackEntry?.savedStateHandle ?: return@LaunchedEffect
        handle.getStateFlow("selectedTagsResult", emptyList<String>())
            .collectLatest { result ->
                if (result.isNotEmpty()) {
                    viewModel.setEditMode(true)
                    viewModel.addTags(result.map { it.removePrefix("#") })
                    handle["selectedTagsResult"] = emptyList<String>() // 재수신 방지
                }
            }
    }

    Scaffold(
        topBar = {
            // uiState.routine이 null이 아닐 때만 TopAppBar를 보여줍니다.
            uiState.routine?.let {
                BasicTopAppBar(
                    title = if (uiState.isEditMode) "루틴 수정" else "내 루틴",
                    navigationIcon = {
                        IconButton(onClick = {
                            if (uiState.isEditMode) {
                                viewModel.cancelEdits()
                            } else {
                                onBackClick()
                            }
                        }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = if (uiState.isEditMode) painterResource(id = R.drawable.ic_x)
                                else painterResource(id = R.drawable.left_arrow),
                                contentDescription = "Back or Close",
                            )
                        }
                    }
                )
            }
        },
        containerColor = Color.White,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.routine == null -> {
                    Text("루틴 정보를 찾을 수 없습니다.", modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    // [추가] 카테고리 변경 시 내부 remember들이 초기화되도록 키 제공
                    key(uiState.routine?.category) { // [추가]
                        MyRoutineDetailContent(
                            viewModel = viewModel,
                            onOpenBottomSheet = { isBottomSheetOpen = true },
                            onCardImageClick = { isImageOptionVisible = true },
                            selectedImageUri = selectedImageUri,
                            onAddTagClick = {
                                if (uiState.isEditMode) {
                                    navController.navigate(Route.TagSearch.createRoute(""))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
    // [변경] 생성 화면과 동일한 이미지 선택 팝업 재사용
    if (isImageOptionVisible) {
        ImageChoiceOptionButtonScreen(
            onImageSelected = {
                imagePickerLauncher.launch("image/*")
                isImageOptionVisible = false
            },
            onCameraSelected = {
                val permission = Manifest.permission.CAMERA
                val isGranted = ContextCompat.checkSelfPermission(
                    context, permission
                ) == PackageManager.PERMISSION_GRANTED

                if (isGranted) {
                    val uri = createImageUri(context)
                    cameraImageUriState.value = uri
                    takePictureLauncher.launch(uri)
                } else {
                    cameraPermissionLauncher.launch(permission)
                }
                isImageOptionVisible = false
            },
            onCancel = { isImageOptionVisible = false }
        )
    }
    DraggableAppSearchBottomSheet(
        isVisible = isBottomSheetOpen,
        onDismiss = { isBottomSheetOpen = false },
        appList = allApps,
        selectedAppList = selectedAppList,
        onAddApp = { app -> viewModel.addApp(app) },
        onRemoveApp = { app -> viewModel.deleteApp(app) }
    )
}


// [변경] 유틸: 카메라 파일용 Uri 생성
private fun createImageUri(context: Context): Uri {
    val imageFile = File.createTempFile(
        "moru_camera_", ".jpg",
        context.cacheDir
    )
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}

@Preview(showBackground = true, name = "상세 화면 - 보기 모드")
@Composable
private fun MyRoutineDetailScreenPreview_ViewMode() {
    MORUTheme {
        val viewModel: MyRoutineDetailViewModel = viewModel()
        viewModel.loadRoutine("routine-501")

        val navController = NavHostController(LocalContext.current)

        MyRoutineDetailScreen(
            routineId = "routine-501",
            onBackClick = {},
            viewModel = viewModel,
            navController = navController
        )
    }
}

@Preview(showBackground = true, name = "상세 화면 - 수정 모드")
@Composable
private fun MyRoutineDetailScreenPreview_EditMode() {
    MORUTheme {
        val viewModel: MyRoutineDetailViewModel = viewModel()
        viewModel.loadRoutine("routine-501")
        viewModel.setEditMode(true) // 프리뷰를 위해 수정 모드로 설정
        val navController = NavHostController(LocalContext.current)

        MyRoutineDetailScreen(
            routineId = "routine-501",
            onBackClick = {},
            viewModel = viewModel,
            navController = navController
        )
    }
}

package com.konkuk.moru.presentation.routinecreate.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.konkuk.moru.R
import com.konkuk.moru.core.component.ImageChoiceOptionButtonScreen
import com.konkuk.moru.core.component.Switch.RoutineSimpleFocusSwitch
import com.konkuk.moru.core.component.routinedetail.AddStepButton
import com.konkuk.moru.core.component.routinedetail.DraggableAppSearchBottomSheet
import com.konkuk.moru.core.component.routinedetail.MyRoutineTagInCreateRoutine
import com.konkuk.moru.core.component.routinedetail.RoutineDescriptionField
import com.konkuk.moru.core.component.routinedetail.RoutineImageSelectBox
import com.konkuk.moru.core.component.routinedetail.SelectUsedAppSection
import com.konkuk.moru.core.component.routinedetail.ShowUserCheckbox
import com.konkuk.moru.core.component.routinedetail.durationpicker.DurationPickerDialog
import com.konkuk.moru.presentation.navigation.Route
import com.konkuk.moru.presentation.routinecreate.component.StepItem
import com.konkuk.moru.presentation.routinecreate.viewmodel.RoutineCreateViewModel
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("DefaultLocale")
@Composable
fun RoutineCreateScreen(
    navController: NavHostController,
    viewModel: RoutineCreateViewModel = hiltViewModel()
//    viewModel: RoutineCreateViewModel? = null
) {
    val focusManager = LocalFocusManager.current
    val isFocusingRoutine by viewModel.isFocusingRoutine
    val showUser by viewModel.showUser
    val routineDescription by viewModel.routineDescription
    val tagList = viewModel.tagList
    val stepList = viewModel.stepList

    var isImageOptionVisible by remember { mutableStateOf(false) }
    var isTimePickerVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val cameraImageUriState = remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.imageUri.value = cameraImageUriState.value
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createImageUri(context)
            cameraImageUriState.value = uri
            takePictureLauncher.launch(uri)
        } else {
            // 권한 거부 시: 스낵바/토스트 처리 가능
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.imageUri.value = uri
    }

    val stepListScrollState = rememberLazyListState()

    // [수정] 서버/피그마 제약 기반 제출 가능 여부
    val isSubmitEnabled by remember {
        derivedStateOf { viewModel.validateForSubmit() == null }
    }

    // 사용앱 바텀시트
    val coroutineScope = rememberCoroutineScope()
    var isBottomSheetOpen by remember { mutableStateOf(false) }
    val allApps = viewModel.appList
    val selectedAppList = viewModel.selectedAppList

    LaunchedEffect(Unit) {
        viewModel.loadInstalledApps(context)
    }

    LaunchedEffect(Unit) {
        val handle = navController.currentBackStackEntry?.savedStateHandle
        handle?.getStateFlow<List<String>>("selectedTagsResult", emptyList())
            ?.collect { result ->
                if (result.isNotEmpty()) {
                    viewModel.addTags(result)
                    handle["selectedTagsResult"] = emptyList<String>()
                }
            }
    }

    val isSubmitting by viewModel.isSubmitting
    val submitError by viewModel.submitError
    LaunchedEffect(viewModel.createdRoutineId.value) {
        viewModel.createdRoutineId.value?.let {
            Log.d("createroutine", "navigate back after success")
            navController.popBackStack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.veryLightGray)
            .systemBarsPadding()
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = colors.veryLightGray),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_x),
                    contentDescription = "Close",
                    modifier = Modifier
                        .padding(start = 16.dp, end = 22.dp)
                        .size(18.dp)
                        .clickable(
                            indication = null,
                            interactionSource = null
                        ) { navController.popBackStack() }
                )
                Text(text = "루틴 생성", style = typography.desc_M_16)
            }

            // Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.White)
                    .padding(horizontal = 16.dp),
                state = stepListScrollState,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(17.dp))
                    // 이미지 + 설명 영역
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        RoutineImageSelectBox(
                            selectedImageUri = viewModel.imageUri.value,
                            onClick = {
                                isImageOptionVisible = true
                                focusManager.clearFocus()
                            }
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            BasicTextField(
                                value = viewModel.routineTitle.value,
                                onValueChange = {
                                    if (it.length <= 10) viewModel.updateTitle(it) // [수정] 최대 10자
                                },
                                textStyle = typography.title_B_24,
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                decorationBox = { innerTextField ->
                                    if (viewModel.routineTitle.value.isEmpty()) {
                                        Text("루틴 제목", style = typography.title_B_24)
                                    }
                                    innerTextField()
                                }
                            )
                            Spacer(modifier = Modifier.height(9.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                ShowUserCheckbox(
                                    showUser = showUser,
                                    onClick = { viewModel.toggleShowUser() }
                                )
                                RoutineSimpleFocusSwitch(
                                    checked = isFocusingRoutine,
                                    onClick = { viewModel.toggleFocusingRoutine() }
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            RoutineDescriptionField(
                                value = routineDescription,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                onValueChange = {
                                    if (it.length <= 32) viewModel.updateDescription(it) // [수정]
                                }
                            )
                        }
                    }
                }

                item {
                    // 태그
                    MyRoutineTagInCreateRoutine(
                        tagList = tagList,
                        onAddTag = { navController.navigate(Route.RoutineSearch.route) },
                        onDeleteTag = { tag -> viewModel.removeTag(tag) }
                    )
                }

                item { Text("STEP", style = typography.title_B_20) }
                itemsIndexed(
                    items = stepList,
                    key = { _, step -> step.id }
                ) { index, step ->
                    StepItem(
                        title = step.title,
                        timeDisplay = if (isFocusingRoutine) step.time else "",
                        isFocusingRoutine = isFocusingRoutine,
                        stepCount = stepList.size,
                        onTitleChange = { newTitle -> viewModel.updateStepTitle(index, newTitle) }, // [수정] 22자 제한은 VM에서 처리
                        onShowTimePicker = {
                            viewModel.setEditingStep(index)
                            isTimePickerVisible = true
                        },
                        onDelete = {
                            if (viewModel.stepList.size > 3) viewModel.removeStep(index) // [수정] 최소 3개 보장
                        }
                    )
                }

                item {
                    AddStepButton { if (viewModel.stepList.size < 6) viewModel.addStep() } // [수정] 최대 6개
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }

            if (isFocusingRoutine) {
                SelectUsedAppSection(
                    selectedAppList = selectedAppList,
                    onRemove = { app -> viewModel.removeAppFromSelected(app) },
                    onAddApp = { coroutineScope.launch { isBottomSheetOpen = true } }
                )
            }

            // 완료하기 버튼
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        if (isSubmitEnabled && !isSubmitting) colors.limeGreen else colors.lightGray
                    )
                    .clickable(
                        enabled = isSubmitEnabled && !isSubmitting,
                        indication = null,
                        interactionSource = null
                    ) {
                        Log.d(
                            "createroutine",
                            "click submit enabled=$isSubmitEnabled isSubmitting=$isSubmitting " +
                                "tags=${tagList.size} steps=${stepList.size} " +
                                "apps=${selectedAppList.size}"
                        )
                        val imageKey: String? = null
                        viewModel.createRoutine(imageKey)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (isSubmitting) "생성 중..." else "완료하기",
                    style = typography.body_SB_16,
                    color = if (isSubmitEnabled) colors.black else colors.darkGray
                )
            }
        }

        // 이미지 선택 옵션
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

        // 시간 선택 팝업
        if (isTimePickerVisible) {
            DurationPickerDialog(
                initialTime = viewModel.getEditingStepTime(), // "HH:mm:ss" 또는 null
                onConfirm = { h, m, s ->
                    viewModel.confirmTime(h, m, s)
                    isTimePickerVisible = false
                },
                onDismiss = { isTimePickerVisible = false }
            )
        }
    }

    DraggableAppSearchBottomSheet(
        isVisible = isBottomSheetOpen,
        onDismiss = { isBottomSheetOpen = false },
        appList = allApps,
        selectedAppList = selectedAppList,
        onAddApp = { app -> viewModel.addAppToSelected(app) },
        onRemoveApp = { app -> viewModel.removeAppFromSelected(app) },
    )
}

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

@Preview
@Composable
private fun RoutineCreateScreenPreview() {
    val navController = NavHostController(LocalContext.current)
    RoutineCreateScreen(navController = navController)
}

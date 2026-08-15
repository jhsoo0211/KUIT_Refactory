package com.konkuk.moru.presentation.myactivity.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.konkuk.moru.R
import com.konkuk.moru.core.util.modifier.noRippleClickable
import com.konkuk.moru.presentation.myactivity.component.BackTitle
import com.konkuk.moru.presentation.myactivity.component.MyActBirthInputField
import com.konkuk.moru.presentation.myactivity.component.MyActGenderInputField
import com.konkuk.moru.presentation.myactivity.component.MyActNickNameInputField
import com.konkuk.moru.presentation.myactivity.component.MyActSelfIntroductionField
import com.konkuk.moru.presentation.myactivity.component.PhotoButtonModal
import com.konkuk.moru.presentation.myactivity.viewmodel.ActUserViewModel
import com.konkuk.moru.presentation.myactivity.viewmodel.ActUserViewModel.SaveState
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography
import kotlinx.coroutines.delay

@Composable
fun ActProfileScreen(
    navController: NavHostController,
    userViewModel: ActUserViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    // ---- UI state from VM ----
    val nickname by userViewModel.nickname.collectAsState()
    val gender by userViewModel.gender.collectAsState()
    val birthday by userViewModel.birthday.collectAsState()
    val bio by userViewModel.bio.collectAsState()
    val profileImage by userViewModel.profileImageUrl.collectAsState()
    val nicknameStatus by userViewModel.nicknameStatus.collectAsState()
    val saveState by userViewModel.saveState.collectAsState()

    LaunchedEffect(Unit) { userViewModel.loadMe() }

    val isEditMode = rememberSaveable { mutableStateOf(false) }
    val showToast = rememberSaveable { mutableStateOf(false) }
    val showImagePickerSheet = rememberSaveable { mutableStateOf(false) }

    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val cameraUri = remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedImageUri.value = it }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraUri.value != null) {
            selectedImageUri.value = cameraUri.value
        }
    }

    // ---- React to save state (success -> toast & exit edit mode) ----
    LaunchedEffect(saveState) {
        when (saveState) {
            SaveState.Success -> {
                showToast.value = true
                isEditMode.value = false
            }
            else -> Unit
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = 14.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            BackTitle(title = "내 프로필", navController)
            Spacer(modifier = Modifier.height(38.dp))

            // ---- Profile image (read-only for now) ----
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.veryLightGray, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val cleaned = profileImage?.trim()

                    if (cleaned.isNullOrBlank()) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_profile_basic),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(60.dp)
                        )
                    } else {
                        SubcomposeAsyncImage(
                            model = cleaned,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            loading = { CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(22.dp)) },
                            success = { SubcomposeAsyncImageContent() },
                            error = {
                                Image(
                                    painter = painterResource(R.drawable.ic_profile_basic),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                if (isEditMode.value) {
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .offset(x = -4.dp, y = -4.dp)
                            .align(Alignment.BottomEnd)
                            .clickable { showImagePickerSheet.value = true }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_edit_profile),
                            contentDescription = "Edit",
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ---- Edit/Save button ----
            val isSaving = saveState is SaveState.Saving
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(68.dp)
                    .height(32.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(
                        if (isEditMode.value) colors.paleLime else colors.veryLightGray,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .noRippleClickable {
                        if (isSaving) return@noRippleClickable
                        if (isEditMode.value) {
                            // 완료 -> 저장 호출
                            userViewModel.saveMyActProfile()
                        } else {
                            // 수정 시작
                            isEditMode.value = true
                        }
                    }
            ) {
                Text(
                    text = when {
                        isSaving -> "저장중"
                        isEditMode.value -> "완료"
                        else -> "수정"
                    },
                    color = if (isEditMode.value) colors.oliveGreen else colors.black,
                    style = typography.desc_M_16
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ---- Nickname ----
            if (isEditMode.value) {
                MyActNickNameInputField(
                    value = nickname,
                    onValueChange = userViewModel::onNicknameChange,
                    status = nicknameStatus,
                    onClickCheck = userViewModel::checkNickname
                )
            } else {
                OutlinedText("닉네임", nickname)
            }

            Spacer(modifier = Modifier.height(21.dp))

            // ---- Gender ----
            if (isEditMode.value) {
                MyActGenderInputField(
                    selected = gender,                   // "남자"/"여자"
                    onSelect = userViewModel::onGenderChangeKo
                )
            } else {
                OutlinedText("성별", gender)
            }

            Spacer(modifier = Modifier.height(21.dp))

            // ---- Birthday ----
            if (isEditMode.value) {
                MyActBirthInputField(
                    value = birthday,
                    onValueChange = userViewModel::onBirthChangeKo // 점/하이픈 모두 허용, VM에서 점포맷 유지
                )
            } else {
                OutlinedText("생년월일", birthday)
            }

            Spacer(modifier = Modifier.height(21.dp))

            // ---- Bio ----
            if (isEditMode.value) {
                MyActSelfIntroductionField(
                    value = bio,
                    onValueChange = userViewModel::onBioChange,
                    maxLength = 20
                )
            } else {
                OutlinedText("자기소개", bio)
            }
        }
    }

    // ---- Success toast ----
    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = showToast.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp)
                    .background(colors.black, shape = RoundedCornerShape(10.dp))
                    .height(80.dp)
            ) {
                Text(
                    text = "수정되었습니다",
                    color = Color.White,
                    style = typography.desc_M_14
                )
            }
        }
    }

    // Auto-hide toast
    LaunchedEffect(showToast.value) {
        if (showToast.value) {
            delay(1000L)
            showToast.value = false
        }
    }

    if (showImagePickerSheet.value) {
        PhotoButtonModal(
            showImagePickerSheet = showImagePickerSheet,
            galleryLauncher = galleryLauncher,
            cameraLauncher = cameraLauncher,
            cameraUri = cameraUri
        )
    }
}

@Composable
fun OutlinedText(title: String, text: String) {
    Text(
        text = title,
        color = colors.black,
        style = typography.body_SB_16,
        modifier = Modifier.padding(bottom = 6.dp)
    )
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(1.dp, colors.lightGray, RoundedCornerShape(4.dp))
            .padding(start = 16.dp)
    ) {
        Text(
            text = text,
            style = typography.desc_M_14,
            color = colors.mediumGray
        )
    }
}

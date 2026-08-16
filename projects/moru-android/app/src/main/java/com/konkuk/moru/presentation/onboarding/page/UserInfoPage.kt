package com.konkuk.moru.presentation.onboarding.page

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.core.component.ProfileSettingCard
import com.konkuk.moru.core.component.TopBarLogoWithTitle
import com.konkuk.moru.presentation.onboarding.component.BirthdayField
import com.konkuk.moru.presentation.onboarding.component.GenderSelection
import com.konkuk.moru.presentation.onboarding.component.IntroductionField
import com.konkuk.moru.presentation.onboarding.component.NickNameTextField
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography
import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.konkuk.moru.core.component.ImageChoiceOptionButtonScreen
import com.konkuk.moru.core.component.button.MoruButtonTypeA
import com.konkuk.moru.presentation.onboarding.OnboardingViewModel
import java.util.Calendar
import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import com.konkuk.moru.presentation.onboarding.model.NickNameValid

@Composable
fun UserInfoPage(
    onNext: () -> Unit,
//    viewModel: OnboardingViewModel? = null, //제발 에뮬레이터 돌릴 땐 hilt로 바꾸는거 잊지 말자 제발
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var nickName by remember { mutableStateOf("") }
    val nicknameAvailable by viewModel?.nicknameAvailable?.collectAsState()
        ?: remember { mutableStateOf<NickNameValid>(NickNameValid.EMPTY) }
//    val nicknameAvailable = NickNameValid.INVALID

    var gender by remember { mutableStateOf("") }
    var birthDay by remember { mutableStateOf("") }
    var introduction by remember { mutableStateOf("") }

//    val isFormValid = isNickNameValid && gender.isNotBlank() && birthDay.isNotBlank()
    val isFormValid =
        (nicknameAvailable == NickNameValid.VALID) && gender.isNotBlank() && birthDay.isNotBlank()
    var isImageOptionVisible by remember { mutableStateOf(false) }

    // [추가] 선택/촬영한 이미지 URI 상태
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) } // ★ 변경 포인트

    // 생년월일
    val calendar = remember { Calendar.getInstance() }
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = "%04d-%02d-%02d".format(year, month + 1, dayOfMonth)
                birthDay = selectedDate
                viewModel?.updateBirthday(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    // -------------- [추가] 사진 선택/촬영 런처 세팅 시작 -------------- //
    // 포토 피커(앨범) — API 33+는 시스템 포토 피커, 이하에선 문서 선택 → 저장소 권한 불필요
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            viewModel?.uploadProfileImageFromUri(context, uri)
        }
        isImageOptionVisible = false
    }

    // 카메라 촬영을 위한 임시 파일 URI
    var cameraTempUri by remember { mutableStateOf<Uri?>(null) }

    // 사진 촬영 런처
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraTempUri != null) {
            selectedImageUri = cameraTempUri
            viewModel?.uploadProfileImageFromUri(context, cameraTempUri!!)
        }
        isImageOptionVisible = false
    }

    // CAMERA 권한 런처
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            // 권한 승인 후 바로 촬영 진행
            val uri = createTempImageUri(context)
            cameraTempUri = uri
            takePictureLauncher.launch(uri)
        } else {
            // 권한 거부: 시트만 닫음(필요시 스낵바/다이얼로그 안내 추가 가능)
            isImageOptionVisible = false
        }
    }

    // 이미지 선택/촬영 트리거 함수
    fun onPickFromGallery() {
        pickImageLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    fun onCaptureFromCamera() {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
    // -------------- [추가 끝] 사진 선택/촬영 런처 세팅 -------------- //

    // 닉네임 유효성 검사 결과에 따라 상태 업데이트
//    LaunchedEffect(nicknameAvailable) {
//        isNickNameValid = (nicknameAvailable == true)
//    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.charcoalBlack)
            .systemBarsPadding()
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            TopBarLogoWithTitle()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFFFFFFF))
                    .padding(bottom = 10.dp, top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "안녕하세요! 사용자님을 소개해주세요",
                        style = typography.body_SB_20,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "언제든 수정 가능해요!",
                        style = typography.desc_M_14,
                        color = colors.mediumGray,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // [변경] 프로필 카드에 imageUri 전달
                    ProfileSettingCard(
                        imageUri = selectedImageUri // ★ 선택/촬영 이미지 반영
                    ) {
                        focusManager.clearFocus()
                        isImageOptionVisible = true // 메뉴 띄우기
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "닉네임",
                            style = typography.body_SB_16
                        )
                        if (nicknameAvailable == NickNameValid.VALID) {
                            Text(
                                text = "사용 가능한 닉네임입니다.",
                                style = typography.desc_M_12,
                                color = colors.limeGreen
                            )
                        } else if (nicknameAvailable == NickNameValid.INVALID) {
                            Text(
                                text = "이미 사용 중인 닉네임입니다.",
                                style = typography.desc_M_12,
                                color = colors.red
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    NickNameTextField(
                        value = nickName,
                        onValueChange = { new ->
                            nickName = new
                            viewModel?.clearNickNameValidity()
                        },
                        onCheckValidNickname = {
                            viewModel?.checkNicknameAvailability(nickName)
                        },
                        validity = nicknameAvailable,
                        placeholder = "닉네임",
                        keyboardType = KeyboardType.Text
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "성별",
                        style = typography.body_SB_16,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    GenderSelection(
                        selectedGender = gender,
                        onGenderSelect = {
                            gender = it
                            viewModel?.updateGender(it)
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "생년월일",
                        style = typography.body_SB_16,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    BirthdayField(birthday = birthDay, onClick = { datePickerDialog.show() })
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "자기소개",
                        style = typography.body_SB_16,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    IntroductionField(
                        value = introduction,
                        onValueChange = {
                            introduction = it
                            viewModel?.updateIntroduction(it)
                        }
                    )
                }

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_onboarding_statusbar1),
                        contentDescription = "status bar",
                        modifier = Modifier.width(134.dp)
                    )
                    Spacer(modifier = Modifier.height(35.dp))
                    MoruButtonTypeA(text = "다음", enabled = isFormValid) {
                        viewModel?.submitUserInfo()
                        onNext()
                    }
                }
            }
        }
        if (isImageOptionVisible) {
            ImageChoiceOptionButtonScreen(
                onImageSelected = {
                    onPickFromGallery() // ★ 실제 동작
                },
                onCameraSelected = {
                    onCaptureFromCamera() // ★ 실제 동작
                },
                onCancel = { isImageOptionVisible = false }
            )
        }
    }
}

/**
 * [추가] 촬영 이미지 저장용 임시 파일 URI 생성 (FileProvider 사용)
 */
private fun createTempImageUri(context: android.content.Context): Uri {
    val time =
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
    val imageFile = File.createTempFile("IMG_${time}_", ".jpg", context.cacheDir) // 캐시 폴더 사용
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}

@Preview
@Composable
private fun UserInfoPagePreview() {
    UserInfoPage(onNext = {})
}
package com.konkuk.moru.presentation.onboarding

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.moru.core.datastore.OnboardingPreference
import com.konkuk.moru.data.dto.request.FavoriteTagRequest
import com.konkuk.moru.domain.repository.OBUserRepository
import com.konkuk.moru.presentation.onboarding.model.NickNameValid
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

// -------------------------------
// UI 모델
// -------------------------------
data class UserInfo(
    val nickname: String = "",
    val gender: String = "",
    val birthday: String = "",
    val bio: String = "",
    val tags: List<String> = emptyList(),
    val profileImageKey: String? = null
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: OBUserRepository
) : ViewModel() {

    // -------------------------------
    // Const & Logs
    // -------------------------------
    companion object {
        const val LAST_PAGE_INDEX = 6
        private const val TAG = "onboarding"
    }

    // -------------------------------
    // UI 상태
    // -------------------------------
    private val _nicknameAvailable = MutableStateFlow<NickNameValid>(NickNameValid.EMPTY)
    val nicknameAvailable: StateFlow<NickNameValid> = _nicknameAvailable

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage

    private val _isOnboardingComplete = MutableStateFlow(false)
    val isOnboardingComplete: StateFlow<Boolean> = _isOnboardingComplete

    private val _userInfo = MutableStateFlow(UserInfo())
    val userInfo: StateFlow<UserInfo> = _userInfo

    // -------------------------------
    // 태그 캐시: "이름" → "ID"
    // -------------------------------
    private val tagIdMap = MutableStateFlow<Map<String, String>>(emptyMap())

    /** 진입 시점 등에서 호출: /api/tags 프리패치 */
    fun prefetchTags() {
        viewModelScope.launch {
            runCatching { ensureTagIdMap() }
                .onSuccess { Log.d(TAG, "prefetchTags() ok: size=${it.size}") }
                .onFailure {
                    Log.d(
                        TAG,
                        "prefetchTags() failed: exception=${it::class.java.simpleName}"
                    )
                }
        }
    }

    // -------------------------------
    // 유틸
    // -------------------------------
    /** "#라벨" → "라벨", 공백 제거 */
    private fun normalizeTagLabel(labelWithHash: String): String =
        labelWithHash.removePrefix("#").trim()

    // -------------------------------
    // 닉네임
    // -------------------------------
    fun checkNicknameAvailability(nickname: String) {
        val trimmed = nickname.trim()

        // [변경 1] 공백/빈 값 가드: 서버 호출 없이 상태만 EMPTY로 전환
        if (trimmed.isEmpty()) {
            _nicknameAvailable.value = NickNameValid.EMPTY
            return
        }

        viewModelScope.launch {
            userRepository.checkNicknameAvailable(trimmed)
                .onSuccess { ok ->
                    Log.d("onboarding", "checkNicknameAvailability() success: available=$ok")
                    _nicknameAvailable.value = when {
                        ok -> NickNameValid.VALID
                        else -> NickNameValid.INVALID
                    }
                    if (ok) updateNickname(trimmed) // 사용 가능 시 상태 반영
                }
                .onFailure {
                    Log.d("onboarding", "checkNicknameAvailability() failed")
                    // [변경 1-2] 실패 시 보수적으로 INVALID로 표기 (UI에서 빨간 테두리)
                    _nicknameAvailable.value = NickNameValid.INVALID
                }
        }
    }

    // -------------------------------
    // 프로필 제출
    // -------------------------------
    fun submitUserInfo() {
        viewModelScope.launch {
            val info = _userInfo.value

            // [변경 2] 서버 gender 매핑 일원화
            val serverGender = when (info.gender.trim()) {
                "남자" -> "MALE"
                "여자" -> "FEMALE"
                else -> "MALE"
            }

            // [변경 2-1] 선택 필드만 put: 서버 스키마와의 충돌 최소화
            val body = buildMap<String, String> {
                put("nickname", info.nickname.trim())
                put("gender", serverGender)
                put("birthday", info.birthday.trim())
                _userInfo.value.profileImageKey?.takeIf { it.isNotBlank() }?.let {
                    put("profileImageUrl", it)
                }
                info.bio.trim().takeIf { it.isNotEmpty() }?.let {
                    put("bio", it)
                }
            }

            userRepository.updateProfileDynamic(body)
                .onSuccess {
                    Log.d(TAG, "submitUserInfo() success")
                }
                .onFailure {
                    Log.d(TAG, "submitUserInfo() failed")
                }
        }
    }

    // -------------------------------
    // 태그 매핑 & 전송
    // -------------------------------
    /** /api/tags 호출 후 이름→ID 맵을 구성·캐시 */
    private suspend fun ensureTagIdMap(): Map<String, String> {
        if (tagIdMap.value.isNotEmpty()) return tagIdMap.value

        val result = userRepository.getAllTags()
        val map = result
            .onFailure { e ->
                Log.d(
                    TAG,
                    "getAllTags() failed: exception=${e::class.java.simpleName}"
                )
            }
            .getOrElse { emptyList() }
            .associateBy(
                keySelector = { it.name.trim() },   // "공부"
                valueTransform = { it.id }           // UUID
            )

        tagIdMap.value = map
        Log.d(TAG, "ensureTagIdMap() size=${map.size}")
        return map
    }

    fun submitFavoriteTagsByLabels(labelsWithHash: List<String>) {
        viewModelScope.launch {
            val map = ensureTagIdMap()
            if (map.isEmpty()) {
                Log.d(TAG, "submitFavoriteTagsByLabels() aborted: tagIdMap is empty")
                return@launch
            }

            val ids = labelsWithHash
                .map(::normalizeTagLabel)
                .mapNotNull { map[it] }
                .distinct()

            if (ids.isEmpty()) {
                Log.d(TAG, "submitFavoriteTagsByLabels() aborted: no matching ids")
                return@launch
            }

            userRepository.addFavoriteTags(FavoriteTagRequest(ids))
                .onSuccess { Log.d(TAG, "addFavoriteTags() success") }
                .onFailure { e ->
                    Log.d(
                        TAG,
                        "addFavoriteTags() failed: exception=${e::class.java.simpleName}"
                    )
                }
        }
    }

    /** (호환) 이미 ID가 있는 경우 그대로 전송 */
    fun submitFavoriteTags(tagIds: List<String>) {
        viewModelScope.launch {
            userRepository.addFavoriteTags(FavoriteTagRequest(tagIds))
                .onFailure { e ->
                    Log.d(
                        TAG,
                        "submitFavoriteTags() failed: exception=${e::class.java.simpleName}"
                    )
                }
        }
    }

    // -------------------------------
    // UI 상태 업데이트
    // -------------------------------
    fun updateNickname(nickname: String) {
        _userInfo.value = _userInfo.value.copy(nickname = nickname)
    }

    fun updateGender(gender: String) {
        _userInfo.value = _userInfo.value.copy(gender = gender)
    }

    fun updateBirthday(birthday: String) {
        _userInfo.value = _userInfo.value.copy(birthday = birthday)
    }

    fun updateIntroduction(intro: String) {
        _userInfo.value = _userInfo.value.copy(bio = intro)
    }

    fun updateTags(tags: List<String>) {
        _userInfo.value = _userInfo.value.copy(tags = tags)
    }

    fun clearNickNameValidity() {
        _nicknameAvailable.value = NickNameValid.EMPTY
    }

    // -------------------------------
    // 이미지 업로드
    // -------------------------------
    fun uploadProfileImageFromUri(context: Context, uri: Uri) {
        viewModelScope.launch {
            val tmpFile = copyUriToCacheFile(context, uri)
            userRepository.uploadImage(tmpFile)
                .onSuccess { imageUrlOrKey ->
                    updateProfileImageUrl(imageUrlOrKey) // 서버는 temp/... 경로를 profileImageUrl로 받음
                    Log.d(TAG, "uploadProfileImageFromUri() success")
                }
                .onFailure {
                    Log.d(TAG, "uploadProfileImageFromUri() failed")
                }
        }
    }

    fun updateProfileImageUrl(url: String?) {
        _userInfo.value = _userInfo.value.copy(profileImageKey = url)
    }

    fun updateProfileImageKey(key: String?) {
        updateProfileImageUrl(key)
    }

    private fun copyUriToCacheFile(context: Context, uri: Uri): File {
        val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
        context.contentResolver.openInputStream(uri).use { input ->
            file.outputStream().use { output -> input?.copyTo(output) }
        }
        return file
    }

    // -------------------------------
    // 온보딩 흐름
    // -------------------------------
    fun nextPage() {
        if (_currentPage.value < LAST_PAGE_INDEX) _currentPage.value += 1
        else completeOnboarding()
    }

    fun skipOnboarding() = completeOnboarding()

    private fun completeOnboarding() {
        viewModelScope.launch {
            OnboardingPreference.setOnboardingComplete(context)
            _isOnboardingComplete.value = true
            Log.d(TAG, "✅ 온보딩 완료됨")
        }
    }
}

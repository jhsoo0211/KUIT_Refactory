package com.konkuk.moru.presentation.myactivity.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.moru.data.repositoryimpl.MyActUserRepository
import com.konkuk.moru.domain.repository.UserRepository
import com.konkuk.moru.presentation.myactivity.component.MyActNicknameStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@HiltViewModel
class ActUserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val myActRepo: MyActUserRepository
) : ViewModel() {

    private val ISO = DateTimeFormatter.ISO_LOCAL_DATE                  // "yyyy-MM-dd"
    private val DOT = DateTimeFormatter.ofPattern("yyyy.MM.dd")         // "yyyy.MM.dd"

    // UI 상태(화면 친화형)
    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname.asStateFlow()

    private val _gender = MutableStateFlow("")          // "남자"/"여자"
    val gender: StateFlow<String> = _gender.asStateFlow()

    private val _birthday = MutableStateFlow("")        // "yyyy.MM.dd"
    val birthday: StateFlow<String> = _birthday.asStateFlow()

    private val _bio = MutableStateFlow("")
    val bio: StateFlow<String> = _bio.asStateFlow()

    private val _profileImageUrl = MutableStateFlow<String?>(null)
    val profileImageUrl: StateFlow<String?> = _profileImageUrl.asStateFlow()

    private val _routineCount = MutableStateFlow(0)
    val routineCount: StateFlow<Int> = _routineCount.asStateFlow()
    private val _followerCount = MutableStateFlow(0)
    val followerCount: StateFlow<Int> = _followerCount.asStateFlow()
    private val _followingCount = MutableStateFlow(0)
    val followingCount: StateFlow<Int> = _followingCount.asStateFlow()

    // 닉네임 배지 상태
    private val _nicknameStatus = MutableStateFlow(MyActNicknameStatus.NONE)
    val nicknameStatus: StateFlow<MyActNicknameStatus> = _nicknameStatus.asStateFlow()

    // 저장 상태 (스크린에서 토스트/전환 제어용)
    sealed interface SaveState {
        data object Idle : SaveState
        data object Saving : SaveState
        data object Success : SaveState
        data class Error(val message: String) : SaveState
    }
    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState: StateFlow<SaveState> = _saveState.asStateFlow()

    // -------- 서버 -> UI --------
    fun loadMe() = viewModelScope.launch {
        runCatching { userRepository.getUserProfile() }
            .onSuccess { p ->
                _nickname.value = p.nickname.orEmpty()
                _gender.value = toKoGender(p.gender)             // "MALE"->"남자"
                _birthday.value = serverToUiDate(p.birthday)     // "yyyy-MM-dd"->"yyyy.MM.dd"
                _bio.value = p.bio.orEmpty()
                _profileImageUrl.value = p.profileImageUrl
                _routineCount.value = p.routineCount
                _followerCount.value = p.followerCount
                _followingCount.value = p.followingCount
                _nicknameStatus.value = MyActNicknameStatus.NONE
            }
        .onFailure { e ->
            Log.e("ActUserViewModel", "getUserProfile failed: exception=${e.javaClass.simpleName}")
        }
    }

    // -------- UI -> 서버 --------
    fun saveMyActProfile() {
        if (_saveState.value == SaveState.Saving) return
        _saveState.value = SaveState.Saving

        viewModelScope.launch {
            runCatching {
                val genderServer = toServerGender(_gender.value)      // "남자"->"MALE"
                val birthdayServer = uiToServerDate(_birthday.value)  // 점/하이픈 모두 허용

                myActRepo.updateMe(
                    nickname = _nickname.value.trim(),
                    genderServer = genderServer,
                    birthdayServer = birthdayServer,
                    bio = _bio.value,
                    profileImageUrl = _profileImageUrl.value
                )
            }.onSuccess {
                _saveState.value = SaveState.Success
                loadMe()
            }.onFailure { e ->
                _saveState.value = SaveState.Error(e.message ?: "프로필 수정 실패")
            Log.e("ActUserViewModel", "saveMyActProfile failed: exception=${e.javaClass.simpleName}")
            }
        }
    }

    // ----- 입력 바인딩 -----
    fun onNicknameChange(s: String) {
        _nickname.value = s
        _nicknameStatus.value =
            if (s.isBlank()) MyActNicknameStatus.NONE else MyActNicknameStatus.FOCUS
    }
    fun onGenderChangeKo(s: String) { _gender.value = if (s.startsWith("남")) "남자" else "여자" }
    fun onBirthChangeKo(s: String) { _birthday.value = normalizeToDotDate(s) }
    fun onBioChange(s: String) { _bio.value = s }

    /** 닉네임 중복확인 API 연동 */
    fun checkNickname() {
        val current = _nickname.value.trim()
        if (current.isEmpty()) {
            _nicknameStatus.value = MyActNicknameStatus.NONE
            return
        }
        // 입력 중 표시 유지
        _nicknameStatus.value = MyActNicknameStatus.FOCUS

        viewModelScope.launch {
            runCatching { myActRepo.isNicknameAvailable(current) }
                .onSuccess { available ->
                    _nicknameStatus.value =
                        if (available) MyActNicknameStatus.VALID
                        else MyActNicknameStatus.ERROR
                }
                .onFailure { e ->
                    _nicknameStatus.value = MyActNicknameStatus.ERROR
                Log.e("ActUserViewModel", "checkNickname failed: exception=${e.javaClass.simpleName}")
                }
        }
    }

    // ----- 변환 유틸 -----
    private fun toKoGender(raw: String?): String = when (raw?.trim()?.uppercase()) {
        "MALE" -> "남자"; "FEMALE" -> "여자"; else -> ""
    }
    private fun toServerGender(ko: String): String =
        if (ko.trim().startsWith("남")) "MALE" else "FEMALE"

    private fun parseDateOrNull(text: String, vararg fmts: DateTimeFormatter): LocalDate? {
        for (f in fmts) runCatching { return LocalDate.parse(text, f) }
        return null
    }
    private fun serverToUiDate(raw: String?): String {
        val t = raw?.trim().orEmpty(); if (t.isEmpty()) return ""
        return parseDateOrNull(t, ISO, DOT)?.format(DOT) ?: t
    }
    private fun uiToServerDate(raw: String): String {
        val t = raw.trim()
        return parseDateOrNull(t, DOT, ISO)?.format(ISO)
            ?: throw IllegalArgumentException("Unsupported birthday format: $raw")
    }
    private fun normalizeToDotDate(raw: String): String {
        val t = raw.trim(); if (t.isEmpty()) return ""
        return parseDateOrNull(t, DOT, ISO)?.format(DOT) ?: raw
    }
}

package com.konkuk.moru.data.repositoryimpl

import com.konkuk.moru.data.dto.request.MyActUpdateMeRequest
import com.konkuk.moru.data.service.MyActUserService
import javax.inject.Inject

class MyActUserRepository @Inject constructor(
    private val service: MyActUserService
) {
    suspend fun updateMe(
        nickname: String,
        genderServer: String,   // "MALE"/"FEMALE"
        birthdayServer: String, // "yyyy-MM-dd"
        bio: String,
        profileImageUrl: String?
    ) {
        val body = MyActUpdateMeRequest(
            nickname = nickname,
            gender = genderServer,
            birthday = birthdayServer,
            bio = bio,
            profileImageUrl = profileImageUrl
        )
        val res = service.patchMe(body)
        if (!res.isSuccessful) {
            val raw = res.errorBody()?.string().orEmpty()
            throw IllegalStateException("프로필 수정 실패: ${res.code()} ${res.message()} | $raw")
        }
    }

    suspend fun isNicknameAvailable(nickname: String): Boolean {
        val res = service.checkNickname(nickname.trim())
        if (!res.isSuccessful) {
            val raw = res.errorBody()?.string().orEmpty()
            throw IllegalStateException("닉네임 확인 실패: ${res.code()} ${res.message()} | $raw")
        }
        return res.body()?.available == true
    }
}

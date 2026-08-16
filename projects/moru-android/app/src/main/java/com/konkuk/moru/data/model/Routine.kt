package com.konkuk.moru.data.model

import android.net.Uri
import java.time.DayOfWeek
import java.time.LocalTime

/**
 * 앱 전체에서 사용될 통합 루틴 데이터 클래스
 */
data class Routine(
    // 기본 정보
    val routineId: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val category: String, // 집중, 간편
    val tags: List<String>,

    // 사용자 관련 정보
    val authorId: String,
    val authorName: String,
    val authorProfileUrl: String?,

    // 상태 정보
    var likes: Int,
    var isLiked: Boolean,
    var isBookmarked: Boolean,
    val isRunning: Boolean, // 현재 사용자가 이 루틴을 실행 중인지
    val isSimple: Boolean = false, // 간편/집중 루틴 구분
    var isChecked: Boolean = false, // '내 루틴' 삭제 모드에서 사용
    var scrapCount: Int = 0, // [추가]


    // 시간 정보 (내 루틴)
    val scheduledTime: LocalTime? = null,
    val scheduledDays: Set<DayOfWeek> = emptySet(),
    var isAlarmEnabled: Boolean = false,
    val requiredTime: String = "", // 총 소요시간 (PT30M 형식)

    // 상세 화면용 정보
    val steps: List<RoutineStep> = emptyList(),
    val similarRoutines: List<SimilarRoutine> = emptyList(),

    // 사용앱 관련 정보
    //val usedApps: List<AppInfo> = emptyList()
    val usedApps: List<UsedAppInRoutine> = emptyList()
)




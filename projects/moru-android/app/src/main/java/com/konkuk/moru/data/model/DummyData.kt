package com.konkuk.moru.data.model

import com.konkuk.moru.presentation.routinefeed.data.LiveUserInfo
import com.konkuk.moru.presentation.routinefeed.data.User
import com.konkuk.moru.presentation.routinefeed.screen.follow.FollowRelation
import java.time.DayOfWeek
import java.time.LocalTime


object DummyData {
    // ⬇️ MY_USER_ID 타입을 String으로 변경
    const val MY_USER_ID = "user-moru-252" // 내 유저 아이디를 "user-moru-252"으로 가정

    // ⬇️ FollowRelation의 ID들을 String으로 변경
    val dummyFollowRelations = listOf(
        FollowRelation(followerId = "user-1", followingId = "user-3"),
        FollowRelation(followerId = "user-1", followingId = "user-101"),
        FollowRelation(followerId = "user-1", followingId = "user-102"),
        FollowRelation(followerId = "user-3", followingId = "user-1"),
        FollowRelation(followerId = "user-3", followingId = "user-102"),
        FollowRelation(followerId = "user-2", followingId = "user-101"),
        FollowRelation(followerId = "user-3", followingId = "user-101"),
        FollowRelation(followerId = "user-4", followingId = "user-101"),
    )

    // ⬇️ User의 ID를 String으로 변경
    val dummyUsers = listOf(
        User(
            "user-1",
            "운동하는 제니",
            " 꾸준함이 답이다! 매일 아침 운동 기록",
            "https://images.unsplash.com/photo-1580489944761-15a19d654956"
        ),
        User(
            "user-2",
            "책읽는 라이언",
            "마음의 양식을 쌓는 중. 한 달에 2권 읽기 목표",
            "https://images.unsplash.com/photo-1548142813-c348350df52b"
        ),
        User(
            "user-3",
            "개발자 모루",
            "코드로 세상을 이롭게 하고 싶은 개발자입니다.",
            null
        ),
        User(
            "user-4",
            "요리왕 준",
            "오늘은 내가 요리사! #집밥 #레시피",
            "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d"
        ),
        User(
            "user-101",
            "요가마스터",
            "몸과 마음의 연결, 요가로 찾으세요.",
            "https://images.unsplash.com/photo-1552058544-f2b08422138a"
        ),
        User(
            "user-102",
            "개발왕",
            "1일 1커밋. #TIL #오픈소스",
            "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d"
        ),
        User(
            "user-201",
            "지하철독서왕",
            "이동 시간을 황금으로 만드는 법",
            "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d"
        ),
        User(MY_USER_ID, "MORU (나)", "이 앱의 주인공입니다. 루틴을 만들어봐요!", null)
    )

    val dummyLiveUsers = listOf(
        LiveUserInfo(
            "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            "운동하는 제니",
            "#오운완",
            "https://images.unsplash.com/photo-1580489944761-15a19d654956"
        ),
        LiveUserInfo(
            "3fa85f64-5717-4562-b3fc-2c963f66afa61",
            "책읽는 라이언",
            "#북스타그램",
            "https://images.unsplash.com/photo-1548142813-c348350df52b"
        ),
        LiveUserInfo(
            "3fa85f64-5717-4562-b3fc-2c963f66afa62",
            "개발자 모루",
            "#TIL",
            null
        ),
        LiveUserInfo(
            "3fa85f64-5717-4562-b3fc-2c963f66afa63",
            "요리왕 준",
            "#집밥",
            "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d"
        ),
        LiveUserInfo(
            "3fa85f64-5717-4562-b3fc-2c963f66afa64",
            "요가마스터",
            "#요가",
            "https://images.unsplash.com/photo-1552058544-f2b08422138a"
        ),
        LiveUserInfo(
            "3fa85f64-5717-4562-b3fc-2c963f66afa65",
            "개발왕",
            "#코딩",
            "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d"
        )
    )

    // ⬇️ Routine의 routineId와 authorId를 String으로 변경
    var feedRoutines: MutableList<Routine> = mutableListOf(
        // --- '나'의 루틴 (MY_USER_ID) ---
        Routine(
            routineId = "routine-501",
            title = "MORU의 아침 명상",
            description = "상쾌한 아침을 여는 5분 명상 루틴입니다. 하루를 차분하게 시작하며 마음의 평화를 찾아보세요.",
            imageUrl = "https://images.unsplash.com/photo-1506126613408-4e0e0f7c50e1",
            category = "간편",
            tags = listOf("명상", "아침루틴", "집중"),
            authorId = MY_USER_ID,
            authorName = "MORU (나)",
            authorProfileUrl = null,
            likes = 42,
            isLiked = true,
            isBookmarked = true,
            isRunning = false,
            scheduledTime = LocalTime.of(9, 15),
            scheduledDays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            steps = listOf(
                RoutineStep(name = "편안하게 앉기", duration = "00:30"),
                RoutineStep(name = "호흡에 집중하기", duration = "03:00"),
                RoutineStep(name = "주변 소리 듣기", duration = "01:00"),
                RoutineStep(name = "마무리 스트레칭", duration = "00:30")
            ),
            usedApps = listOf(
//                AppInfo(
//                    name = "Calm",
//                    iconUrl = "https://i.pinimg.com/originals/31/28/90/312890339947b7294633999d1c03387c.png"
//                ),
//                AppInfo(
//                    name = "Youtube",
//                    iconUrl = "https://uxwing.com/wp-content/themes/uxwing/download/brands-and-social-media/youtube-logotype-icon.png"
//                )
                UsedAppInRoutine(
                    appName = "카카오톡",
                    appIcon = placeholderIcon(),
                    packageName = "com.kakao.talk"
                ),
                UsedAppInRoutine(
                    appName = "인스타그램",
                    appIcon = placeholderIcon(),
                    packageName = "com.instagram.android"
                ),
            )
        ),
        Routine(
            routineId = "routine-502",
            title = "MORU의 주말 대청소",
            description = "개운하게 주말을 맞이하는 청소 루틴! 음악과 함께하면 더 즐거워요.",
            imageUrl = "https://images.unsplash.com/photo-1585421943279-25f1712ba7a8",
            category = "집중",
            tags = listOf("청소", "주말", "정리"),
            authorId = MY_USER_ID,
            authorName = "MORU (나)",
            authorProfileUrl = null,
            likes = 15,
            isLiked = false,
            isBookmarked = true,
            isRunning = true,
            scheduledTime = LocalTime.of(10, 25),
            scheduledDays = setOf(DayOfWeek.SATURDAY),
            steps = listOf(
                RoutineStep(name = "환기하기", duration = "05:00"),
                RoutineStep(name = "먼지 털기", duration = "15:00"),
                RoutineStep(name = "청소기 돌리기", duration = "20:00"),
                RoutineStep(name = "물걸레질 하기", duration = "20:00")
            ),
            usedApps = listOf(
                UsedAppInRoutine(
                    appName = "유튜브",
                    appIcon = placeholderIcon(),
                    packageName = "com.google.android.youtube"
                ),
            )
        ),
        Routine(
            routineId = "routine-503",
            title = "MORU의 저녁 스트레칭",
            description = "하루의 피로를 푸는 간단한 스트레칭.",
            imageUrl = "https://images.unsplash.com/photo-1599901860904-17e6ed7083a0",
            category = "간편",
            tags = listOf("스트레칭", "저녁루틴", "건강"),
            authorId = MY_USER_ID,
            authorName = "MORU (나)",
            authorProfileUrl = null,
            likes = 28,
            isLiked = true,
            isBookmarked = false,
            isRunning = false,
            scheduledTime = LocalTime.of(1, 5),
            scheduledDays = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY),
            steps = listOf(
                RoutineStep(name = "목 스트레칭", duration = "02:00"),
                RoutineStep(name = "어깨 돌리기", duration = "02:00"),
                RoutineStep(name = "허리 비틀기", duration = "01:00")
            )
        ),
        Routine(
            routineId = "routine-504",
            title = "MORU의 집중 코딩 타임",
            description = "방해 없이 2시간 동안 코딩에 집중하는 시간. 포모도로 기법 활용!",
            imageUrl = "https://images.unsplash.com/photo-1517694712202-14dd9538aa97",
            category = "집중",
            tags = listOf("코딩", "개발", "집중"),
            authorId = MY_USER_ID,
            authorName = "MORU (나)",
            authorProfileUrl = null,
            likes = 55,
            isLiked = false,
            isBookmarked = true,
            isRunning = false,
            scheduledTime = LocalTime.of(20, 0),
            scheduledDays = setOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY),
            steps = listOf(
                RoutineStep(name = "작업 환경 설정", duration = "05:00"),
                RoutineStep(name = "25분 집중, 5분 휴식 (x4)", duration = "120:00")
            ),
            usedApps = listOf(
//                AppInfo(
//                    name = "Github",
//                    iconUrl = "https://uxwing.com/wp-content/themes/uxwing/download/brands-and-social-media/github-icon.png"
//                )
                UsedAppInRoutine(
                    appName = "GitHub",
                    appIcon = placeholderIcon(),
                    packageName = "com.github.android"
                )
            )
        ),

        // --- 다른 사용자의 루틴 ---
        Routine(
            routineId = "routine-1",
            title = "아침 10분 요가",
            description = "간단한 요가로 하루를 시작해요. 몸과 마음을 깨우는 시간을 가져보세요.",
            imageUrl = "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?q=80&w=2120&auto=format&fit=crop",
            category = "간편",
            tags = listOf("건강", "요가", "아침루틴", "운동", "명상"),
            authorId = "user-101",
            authorName = "요가마스터",
            authorProfileUrl = "https://images.unsplash.com/photo-1552058544-f2b08422138a",
            likes = 112,
            isLiked = true,
            isBookmarked = false,
            isRunning = false,
            steps = listOf(
                RoutineStep(name = "매트 준비", duration = "00:30"),
                RoutineStep(name = "고양이 자세", duration = "02:00"),
                RoutineStep(name = "태양 경배 자세", duration = "05:00"),
                RoutineStep(name = "사바사나 (휴식)", duration = "02:30")
            )
        ),
        Routine(
            routineId = "routine-2",
            title = "매일 TIL 작성하기",
            description = "개발 지식을 매일 기록합니다. 꾸준함이 실력!",
            imageUrl = "https://images.unsplash.com/photo-1498050108023-c5249f4df085?q=80&w=2072&auto=format&fit=crop",
            category = "집중",
            tags = listOf("개발", "TIL"),
            authorId = "user-102",
            authorName = "개발왕",
            authorProfileUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d",
            likes = 98,
            isLiked = false,
            isBookmarked = false,
            isRunning = false,
            steps = listOf(
                RoutineStep(name = "어제 배운 내용 복습", duration = "10:00"),
                RoutineStep(name = "블로그에 TIL 작성", duration = "45:00")
            )
        ),
        // --- [추가] 새로운 루틴 데이터 ---
        Routine(
            routineId = "routine-3",
            title = "제니의 헬스 3분할",
            description = "오늘은 등 운동 하는 날! #오운완",
            imageUrl = "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b",
            category = "집중",
            tags = listOf("헬스", "운동", "등운동"),
            authorId = "user-1",
            authorName = "운동하는 제니",
            authorProfileUrl = "https://images.unsplash.com/photo-1580489944761-15a19d654956",
            likes = 256,
            isLiked = true,
            isBookmarked = true,
            isRunning = false,
            steps = listOf(
                RoutineStep(name = "데드리프트", duration = "20:00"),
                RoutineStep(name = "풀업", duration = "15:00"),
                RoutineStep(name = "바벨 로우", duration = "15:00")
            )
        ),
        Routine(
            routineId = "routine-4",
            title = "저녁 독서 30분",
            description = "잠들기 전, 스마트폰 대신 책과 함께.",
            imageUrl = "https://images.unsplash.com/photo-1532012197267-da84d127e765?q=80&w=1887&auto=format&fit=crop",
            category = "간편",
            tags = listOf("독서", "저녁루틴", "자기계발"),
            authorId = "user-2",
            authorName = "책읽는 라이언",
            authorProfileUrl = "https://images.unsplash.com/photo-1548142813-c348350df52b",
            likes = 76,
            isLiked = false,
            isBookmarked = true,
            isRunning = false,
            steps = emptyList()
        ),
        Routine(
            routineId = "routine-5",
            title = "주간 밀프렙 만들기",
            description = "주말에 미리 준비해서 평일 저녁을 여유롭게!",
            imageUrl = "https://images.unsplash.com/photo-1606787366850-de6330128214",
            category = "집중",
            tags = listOf("요리", "밀프렙", "집밥"),
            authorId = "user-4",
            authorName = "요리왕 준",
            authorProfileUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d",
            likes = 152,
            isLiked = true,
            isBookmarked = false,
            isRunning = false,
            steps = listOf(
                RoutineStep(name = "장보기", duration = "60:00"),
                RoutineStep(name = "채소 손질", duration = "30:00"),
                RoutineStep(name = "메인 요리 조리", duration = "90:00")
            ),
            usedApps = listOf(
//                AppInfo(
//                    name = "오늘의집",
//                    iconUrl = "https://i.pinimg.com/736x/87/a7/9e/87a79e09cc92f802c114325a1215f913.jpg"
//                ),
//                AppInfo(
//                    name = "마켓컬리",
//                    iconUrl = "https://play-lh.googleusercontent.com/F9p-8L9H_nS2z24x6d-L5Z99i5c81NP0yD-cK5Gf_T22mw_s2gT2jCqC-w=w240-h480-rw"
//                )
                UsedAppInRoutine(
                    appName = "오늘의집",
                    appIcon = placeholderIcon(),
                    packageName = "com.ohou.android"
                ),
                UsedAppInRoutine(
                    appName = "마켓컬리",
                    appIcon = placeholderIcon(),
                    packageName = "com.kurly.android"
                )
            )
        )
    )
}

/**
 * 전체 루틴 목록에서 주어진 루틴과 비슷한 태그를 가진 다른 루틴들을 찾아 반환합니다.
 */
fun findSimilarRoutinesByTags(
    targetRoutine: Routine,
    allRoutines: List<Routine>,
    limit: Int = 5
): List<SimilarRoutine> {
    val targetTags = targetRoutine.tags.toSet()

    // 1. 각 루틴과 겹치는 태그 수를 계산하여 '관련도 점수'를 매깁니다.
    val routinesWithScores = allRoutines
        .filter { it.routineId != targetRoutine.routineId } // 자기 자신은 제외
        .mapNotNull { routine ->
            val matchingTags = routine.tags.intersect(targetTags)
            if (matchingTags.isNotEmpty()) {
                // (루틴, 점수, 실제 겹치는 태그) 튜플로 만듭니다.
                Triple(routine, matchingTags.size, matchingTags.first())
            } else {
                null
            }
        }

    // 2. 관련도 점수(겹치는 태그 수)가 높은 순으로 정렬합니다.
    val sortedRoutines = routinesWithScores.sortedByDescending { it.second }

    // 3. 주어진 limit만큼 잘라냅니다.
    return sortedRoutines
        .take(limit)
        .map { (routine, _, firstMatchingTag) ->
            // 4. SimilarRoutine 객체로 변환할 때, 실제 겹치는 태그를 표시합니다.
            SimilarRoutine(
                id = routine.routineId,
                imageUrl = routine.imageUrl,
                name = routine.title,
                tag = "#$firstMatchingTag"
            )
        }
}
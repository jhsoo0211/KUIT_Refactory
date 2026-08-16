package com.konkuk.moru.presentation.navigation

import android.net.Uri
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Route(
    val route: String
) {
    data object AuthCheck : Route("auth_check")
    data object Login : Route("login")
    data object SignUp : Route("sign_up")
    data object Main : Route("main")
    data object Onboarding : Route("onboarding")

    data object Home : Route(route = "home")

    // 루틴 시작 전 소개 화면
    data object RoutineFocusIntro : Route("routine_focus_intro")

    // 실제 루틴 실행 화면 (집중 루틴 시-몰입화면)
    data object RoutineFocus : Route("routine_focus")

    // 실제 루틴 실행 화면 (간편 루틴 화면)
    data object RoutineSimpleRun : Route("routine_simple_run")

    data object RoutineFeed : Route(route = "routine_feed")

    data object RoutineSearch : Route("routine_search")
    data object RoutineFeedDetail : Route(route = CanonicalRoutes.ROUTINE_FEED_DETAIL_PATTERN) {
        const val KEY = CanonicalRoutes.ROUTINE_ID_ARGUMENT

        /** Callers pass a raw server ID; path-segment encoding happens here exactly once. */
        fun createRoute(routineId: String) = CanonicalRoutes.routineFeedDetail(routineId)
    }

    data object RoutineFeedRec : Route(route = "routine_feed_rec/{title}") {
        fun createRoute(title: String): String {
            val encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
            return "routine_feed_rec/$encodedTitle"
        }
    }

    object Follow : Route("follow/{userId}/{selectedTab}") {
        fun createRoute(userId: String?, selectedTab: String) = "follow/$userId/$selectedTab"
    }

    // The profile owner is explicit so the destination remains restorable.
    object UserProfile : Route("user_profile/{userId}") {
        fun createRoute(userId: String) = "user_profile/$userId"
    }


    object RoutineDetail {
        const val KEY = "routineId" // ✅ 키 상수화
        const val route = "routine_detail/{$KEY}"
        fun create(routineId: String) = "routine_detail/${Uri.encode(routineId)}"
    }

    data object MyRoutineDetail : Route("my_routine_detail/{routineId}") { // ← 리터럴로 넣기
        const val KEY = "routineId"
        fun createRoute(routineId: String) = "my_routine_detail/${Uri.encode(routineId)}"
    }

    data object MyRoutine : Route(route = "my_routine")

    data object MyActivity : Route(route = "my_activity")

    data object Notification : Route("notification")

    data object ActSetting : Route(route = "act_setting")
    data object ActScrab : Route(route = "act_scrab")
    data object ActFabTag : Route(route = "act_fab_tag")
    data object ActProfile : Route(route = "act_profile")
    data object ActRecord : Route(route = "act_record")
    data object ActRecordDetail : Route(route = "act_record_detail/{logId}") {
        fun createRoute(logId: String): String {
            // UUID라도 안전하게 인코딩
            return "act_record_detail/${Uri.encode(logId)}"
        }
    }
    data object ActPolicy : Route(route = "act_policy")

    data object ActInsightInfo : Route(route = "act_insight")

    // 루틴 생성 화면
    data object RoutineCreate : Route(route = "routine_create")


    object TagSearch {
        const val route = "tag_search"

        // 이동용 URL 생성기
        fun createRoute(originalQuery: String = ""): String =
            "tag_search?originalQuery=${java.net.URLEncoder.encode(originalQuery, "UTF-8")}"
    }

    // Tag search has separate state and therefore uses its own destination.
    data object RoutineTagSearch : Route("routine_tag_search")

    object RoutineFeedDetail1 {
        const val base = "routineFeedDetail"
        const val arg = "routineId"
        const val pattern = "$base/{$arg}"           // NavGraph 등록용
        fun withId(id: String) = "$base/${Uri.encode(id)}" // navigate용
    }
}

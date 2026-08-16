package com.konkuk.moru.presentation.onboarding.permission

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.konkuk.moru.presentation.onboarding.model.PermissionType

class PermissionController(
    private val context: Context,
    private val notificationLauncher: ManagedActivityResultLauncher<String, Boolean>,
    private val isPreview: Boolean
) {
    val states = mutableStateMapOf<PermissionType, Boolean>().apply {
        PermissionType.entries.forEach { put(it, false) }
    }

    fun onClick(type: PermissionType) {
        when (type) {
            PermissionType.PUSH_NOTIFICATION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val granted = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                    if (granted) {
                        states[PermissionType.PUSH_NOTIFICATION] = true
                    } else {
                        notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                } else {
                    // Android 12 이하는 런타임 권한 대신 앱 알림 설정 상태를 사용한다.
                    states[PermissionType.PUSH_NOTIFICATION] =
                        areNotificationsEnabledCompat(context)
                }
            }

            PermissionType.DO_NOT_DISTURB -> {
                val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (!nm.isNotificationPolicyAccessGranted) {
                    context.startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
                }
            }
        }
    }

    fun refresh() {
        if (isPreview) return // 프리뷰에선 시스템 접근 금지

        val pushGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            areNotificationsEnabledCompat(context)
        }
        states[PermissionType.PUSH_NOTIFICATION] = pushGranted

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        states[PermissionType.DO_NOT_DISTURB] = nm.isNotificationPolicyAccessGranted
    }

    private fun areNotificationsEnabledCompat(context: Context): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

}

@Composable
fun rememberPermissionController(): PermissionController {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // 런처 결과를 현재 컨트롤러 상태에 반영하기 위한 참조다.
    val controllerState = remember { mutableStateOf<PermissionController?>(null) }

    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ ->
        controllerState.value?.refresh()
    }

    val controller = remember(context, isPreview, notificationLauncher) {
        PermissionController(context, notificationLauncher, isPreview)
    }
    LaunchedEffect(controller) { controllerState.value = controller }

    // 최초 진입 때 갱신
    LaunchedEffect(Unit) {
        controller.refresh()
    }

    // 설정에서 복귀 시 갱신
    DisposableEffect(lifecycleOwner) {
        if (!isPreview) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    controller.refresh()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        } else {
            onDispose {}
        }
    }

    return controller
}

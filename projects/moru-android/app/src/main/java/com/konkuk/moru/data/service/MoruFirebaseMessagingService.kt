package com.konkuk.moru.data.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.konkuk.moru.MainActivity
import com.konkuk.moru.R
import com.konkuk.moru.domain.repository.FcmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint // Hilt 주입을 위해 어노테이션 추가
class MoruFirebaseMessagingService : FirebaseMessagingService() {

    @Inject // Repository 주입
    lateinit var fcmRepository: FcmRepository
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // 새로 발급된 토큰을 우리 서버로 전송합니다.
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        // ViewModel이 아닌 Service에서 직접 코루틴을 관리합니다.
        serviceScope.launch {
            try {
                // FcmRepository를 통해 서버 API를 호출합니다.
                fcmRepository.registerFcmToken(token)
                Log.d("FCM_TOKEN_API", "FCM 토큰을 서버에 성공적으로 등록했습니다.")
            } catch (e: Exception) {
                Log.e("FCM_TOKEN_API", "FCM 토큰 서버 등록 실패: exception=${e.javaClass.simpleName}")
                // TODO: 등록 실패 시, 나중에 재시도하는 로직을 추가할 수 있습니다.
            }
        }
    }

    // onMessageReceived, sendNotification 함수는 기존과 동일
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // ✅ 데이터 페이로드에서 routineId 추출
        val routineId = remoteMessage.data["routineId"]

        remoteMessage.notification?.let {
            val title = it.title ?: "Moru"
            val body = it.body ?: "새로운 알림이 도착했습니다."

            // ✅ sendNotification 함수에 routineId 전달
            sendNotification(title, body, routineId)
        }
    }

    // ✅ sendNotification 함수가 routineId를 받도록 수정
    private fun sendNotification(title: String, messageBody: String, routineId: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // ✅ Intent에 routineId를 추가 데이터로 담기
            putExtra("ROUTINE_ID", routineId)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_moru)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent) // 사용자가 알림을 탭하면 이 pendingIntent가 실행됨
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "기본 알림",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(0, notificationBuilder.build())
        }
    }
}

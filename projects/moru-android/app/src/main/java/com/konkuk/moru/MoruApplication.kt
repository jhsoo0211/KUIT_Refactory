package com.konkuk.moru

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // ✅ Hilt를 사용하려면 Application 클래스에 이 어노테이션이 필수입니다.
class MoruApplication : Application()
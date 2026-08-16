package com.konkuk.moru.presentation.myactivity.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.moru.domain.model.MyActRecordDetail
import com.konkuk.moru.domain.repository.MyActRecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

@HiltViewModel
class MyActRecordDetailViewModel @Inject constructor(
    private val repo: MyActRecordRepository
) : ViewModel() {

    private val _detail = MutableStateFlow<MyActRecordDetail?>(null)
    val detail: StateFlow<MyActRecordDetail?> = _detail

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun load(id: String) {
        if (_loading.value) return
        _loading.value = true
        viewModelScope.launch {
            runCatching { repo.getLogDetail(id) }
                .onSuccess { _detail.value = it; _error.value = null }
                .onFailure { e ->
                    val msg = when (e) {
                        is HttpException -> "요청에 실패했습니다. (HTTP ${e.code()})"
                        is IOException -> "네트워크 연결을 확인해 주세요."
                        is SerializationException -> "서버 응답을 처리하지 못했습니다."
                        else -> "활동 기록을 불러오지 못했습니다."
                    }
                    _error.value = msg
                    Log.e("MyActRecordDetail", "load failed: exception=${e.javaClass.simpleName}")
                }
            _loading.value = false
        }
    }
}

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
                        is HttpException -> {
                            val body = runCatching { e.response()?.errorBody()?.string() }.getOrNull()
                            "HTTP ${e.code()} ${e.message()} ${body ?: ""}".trim()
                        }
                        is IOException -> "네트워크 오류: ${e.localizedMessage ?: e.javaClass.simpleName}"
                        is SerializationException -> "파싱 오류: ${e.localizedMessage ?: e.javaClass.simpleName}"
                        else -> e.localizedMessage ?: e.javaClass.simpleName
                    }
                    _error.value = msg
                    Log.e("MyActRecordDetail", "load failed: exception=${e.javaClass.simpleName}")
                }
            _loading.value = false
        }
    }
}

package com.konkuk.moru.presentation.myactivity.viewmodel

import androidx.lifecycle.ViewModel
import com.konkuk.moru.domain.repository.MyActScrapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.konkuk.moru.domain.model.MyActScrap
import com.konkuk.moru.domain.model.MyActScrapCursor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MyActScrapViewModel @Inject constructor(
    private val repo: MyActScrapRepository
): ViewModel() {

    private val _items = MutableStateFlow<List<MyActScrap>>(emptyList())
    val items: StateFlow<List<MyActScrap>> = _items
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var nextCursor: MyActScrapCursor? = null
    private var hasNext: Boolean = true

    fun loadFirst(size: Int = 21) {
        if (_loading.value) return
        _loading.value = true
        viewModelScope.launch {
            runCatching { repo.getScraps(size = size) }
                .onSuccess { p ->
                    _items.value = p.items; nextCursor = p.nextCursor; hasNext = p.hasNext
                }.onFailure { _error.value = it.message }
            _loading.value = false
        }
    }

    fun loadNext(size: Int = 21) {
        if (_loading.value || !hasNext) return
        val c = nextCursor ?: return
        _loading.value = true
        viewModelScope.launch {
            runCatching { repo.getScraps(c.createdAt, c.scrapId, size) }
                .onSuccess { p ->
                    _items.value = _items.value + p.items; nextCursor = p.nextCursor; hasNext = p.hasNext
                }.onFailure { _error.value = it.message }
            _loading.value = false
        }
    }
}

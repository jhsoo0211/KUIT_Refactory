package com.konkuk.moru.presentation.myactivity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.moru.domain.repository.MyActTagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import com.konkuk.moru.presentation.myactivity.screen.TagDto

@HiltViewModel
class MyActTagViewModel @Inject constructor(
    private val repo: MyActTagRepository
) : ViewModel() {

    private val _allTags = MutableStateFlow<List<TagDto>>(emptyList())
    val allTags: StateFlow<List<TagDto>> = _allTags

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadAllTags() {
        viewModelScope.launch {
            runCatching { repo.getAllTags() }
                .onSuccess { domainTags ->
                    val uiTags = domainTags.mapIndexed { index, t ->
                        TagDto(
                            id = index,
                            name = "#${t.name}",
                            isSelected = false
                        )
                    }
                    _allTags.value = uiTags
                }
                .onFailure { e ->
                    _error.value = e.message ?: "태그를 불러오지 못했습니다."
                }
        }
    }

    fun loadAllTagsAndFavorites() {
        viewModelScope.launch {
            runCatching { repo.getAllTags() }
                .onSuccess { all ->
                    val uiAll = all.mapIndexed { index, t ->
                        TagDto(
                            id = index,
                            name = "#${t.name}",
                            isSelected = false,
                            serverId = t.id
                        )
                    }
                    _allTags.value = uiAll

                    runCatching { repo.getMyFavoriteTags() }
                        .onSuccess { favs ->
                            val favIdSet = favs.map { it.id }.toSet()
                            _allTags.value = _allTags.value.map { dto ->
                                if (favIdSet.contains(dto.serverId)) dto.copy(isSelected = true) else dto
                            }
                        }
                        .onFailure { e ->
                            _error.value = e.message ?: "관심 태그 불러오기 실패"
                        }
                }
                .onFailure { e ->
                    _error.value = e.message ?: "전체 태그 불러오기 실패"
                }
        }
    }

    fun submitFavoriteTags(selectedUiIds: List<Int>) {
        val before = _allTags.value
        val selectedDtos = before.filter { selectedUiIds.contains(it.id) }
        val serverIds = selectedDtos.map { it.serverId }.filter { it.isNotBlank() }
        if (serverIds.isEmpty()) return

        val optimistic = before.map { dto ->
            if (selectedUiIds.contains(dto.id)) dto.copy(isSelected = true) else dto
        }
        _allTags.value = optimistic

        viewModelScope.launch {
            runCatching { repo.setMyFavoriteTags(serverIds) }
                .onFailure { e ->
                    _allTags.value = before
                    _error.value = e.message ?: "관심 태그 저장 실패"
                }
        }
    }

    fun removeFavoriteTag(uiId: Int) {
        val before = _allTags.value
        val target = before.firstOrNull { it.id == uiId } ?: return
        val tagId = target.serverId
        if (tagId.isBlank()) return

        val optimistic = before.map { dto ->
            if (dto.id == uiId) dto.copy(isSelected = false) else dto
        }
        _allTags.value = optimistic

        viewModelScope.launch {
            runCatching { repo.deleteMyFavoriteTag(tagId) }
                .onFailure { e ->
                    // 실패하면 롤백 + 에러 메시지
                    _allTags.value = before
                    _error.value = e.message ?: "관심 태그 삭제 실패"
                }
        }
    }
}

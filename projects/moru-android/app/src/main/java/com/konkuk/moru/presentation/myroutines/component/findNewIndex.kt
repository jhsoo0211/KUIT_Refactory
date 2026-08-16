package com.konkuk.moru.presentation.myroutines.component

import androidx.compose.foundation.lazy.LazyListState
import kotlin.math.abs


fun findNewIndex(
    draggedItemIndex: Int,
    verticalDragOffset: Float,
    listState: LazyListState,
    headerItemCount: Int
): Int? {
    val layoutInfo = listState.layoutInfo
    // 실제 LazyColumn에서의 아이템 인덱스 (헤더 개수 더하기)
    val absoluteDraggedItemIndex = draggedItemIndex + headerItemCount

    val draggedItem = layoutInfo.visibleItemsInfo.find { it.index == absoluteDraggedItemIndex } ?: return null
    val draggedItemCenter = draggedItem.offset + draggedItem.size / 2 + verticalDragOffset

    return layoutInfo.visibleItemsInfo
        .filter {
            // STEP 리스트 범위 내에 있고, 자기 자신이 아닌 아이템만 필터링
            it.index >= headerItemCount && it.index < layoutInfo.totalItemsCount && it.index != absoluteDraggedItemIndex
        }
        .minByOrNull {
            val itemCenter = it.offset + it.size / 2
            abs(itemCenter - draggedItemCenter)
        }
        // 찾은 아이템의 인덱스에서 헤더 개수를 빼서 실제 데이터 리스트의 인덱스로 변환
        ?.index?.minus(headerItemCount)
}

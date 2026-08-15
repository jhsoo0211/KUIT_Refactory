package com.konkuk.moru.presentation.routinefeed.component.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.konkuk.moru.ui.theme.MORUTheme

@Composable
internal fun RecentSearchesHeader(onDeleteAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "최근 검색어", style = MORUTheme.typography.body_SB_14)
        Text(
            text = "전체 삭제",
            style = MORUTheme.typography.desc_M_12,
            color = MORUTheme.colors.mediumGray,
            modifier = Modifier.clickable { onDeleteAll() }
        )
    }
}
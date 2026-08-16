package com.konkuk.moru.presentation.routinefeed.component.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme

/**
 * 최근 검색어 아이템을 표시하는 컴포저블
 *
 * @param searchText 표시될 검색어 텍스트
 * @param date 검색 날짜
 * @param onItemClick 검색어 아이템 전체를 클릭했을 때의 동작
 * @param onDeleteClick 'x' 삭제 버튼을 클릭했을 때의 동작
 * @param modifier Modifier
 */
@Composable
fun RecentSearchItem(
    searchText: String,
    date: String,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. 왼쪽: 히스토리 아이콘 + 검색어
        Icon(
            modifier=Modifier.size(23.dp),
            painter = painterResource(R.drawable.ic_searchhistory),
            contentDescription = "최근 검색",
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = searchText,
            style = MORUTheme.typography.desc_M_16,
            color = MORUTheme.colors.black
        )

        // 2. 중앙: 가변 공간
        Spacer(modifier = Modifier.weight(1f))

        // 3. 오른쪽: 날짜 + 삭제 버튼
        Text(
            text = date,
            style = MORUTheme.typography.desc_M_16,
            color= Color(0xFF595959)
        )
        Spacer(modifier = Modifier.width(8.dp))
        // IconButton을 사용해 클릭 영역을 확보합니다.
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.size(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "검색 기록 삭제",
                tint = Color(0xFF595959)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecentSearchItemPreview() {
    MORUTheme {
        RecentSearchItem(
            searchText = "검색어",
            date = "05.14.",
            onItemClick = {},
            onDeleteClick = {}
        )
    }
}
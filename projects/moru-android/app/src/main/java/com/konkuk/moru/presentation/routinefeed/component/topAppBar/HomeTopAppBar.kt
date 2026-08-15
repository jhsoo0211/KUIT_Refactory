package com.konkuk.moru.presentation.routinefeed.component.topAppBar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme

/**
 * 홈 화면에서 사용하는 TopAppBar (클릭 시 화면 이동 버전)
 * @param onSearchClick 검색 바 클릭 시 동작
 * @param hasNotification 새 알림이 있는지 여부
 * @param onLogoClick 로고 클릭 시 동작
 * @param onNotificationClick 알림 아이콘 클릭 시 동작
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    onSearchClick: () -> Unit,
    hasNotification: Boolean,
    onLogoClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            MoruSearchBar(
                query = "",
                onQueryChange = {},
                onSearch = {},
                onClick = onSearchClick,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            )
        },
        modifier = modifier.fillMaxWidth(),
        navigationIcon = {
            IconButton(onClick = onLogoClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_moru),
                    contentDescription = "로고",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(42.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "검색",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = onNotificationClick) {
                Icon(
                    painter = painterResource(
                        id = if (hasNotification) R.drawable.ic_bell_on else R.drawable.ic_bell_off
                    ),
                    tint = Color.Unspecified,
                    contentDescription = "알림",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF212120),
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeTopAppBarPreview() {
    MORUTheme {
        HomeTopAppBar(
            onSearchClick = {},
            hasNotification = true,
            onLogoClick = {},
            onNotificationClick = {}
        )
    }
}
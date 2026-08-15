package com.konkuk.moru.core.component

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.presentation.navigation.Route
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme.colors

@Composable
fun MoruBottomBar(
    modifier: Modifier = Modifier,
    selectedRoute: String,
    onItemSelected: (String) -> Unit,
    onIconMeasured: (Int, String, Offset) -> Unit
) {
    val items = listOf(
        BottomNavItem(
            title = "홈",
            route = Route.Home.route,
            iconResId = R.drawable.ic_home_un,
            selectedIconResId = R.drawable.ic_home
        ),
        BottomNavItem(
            title = "루틴 피드",
            route = Route.RoutineFeed.route,
            iconResId = R.drawable.ic_routine_feed_un,
            selectedIconResId = R.drawable.ic_routine_feed
        ),
        BottomNavItem(
            title = "내 루틴",
            route = Route.MyRoutine.route,
            iconResId = R.drawable.ic_my_routine_un,
            selectedIconResId = R.drawable.ic_my_routine
        ),
        BottomNavItem(
            title = "내 활동",
            route = Route.MyActivity.route,
            iconResId = R.drawable.ic_my_activity_un,
            selectedIconResId = R.drawable.ic_my_activity
        )
    )

    NavigationBar(
        modifier = modifier
            .navigationBarsPadding()
            .height(80.dp),
        containerColor = Color.White
    ) {
        items.forEachIndexed { idx, item ->
            val isSelected = selectedRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(item.route) },
                interactionSource = remember { MutableInteractionSource() },
                modifier = Modifier.onGloballyPositioned { c ->
                    val pos = c.positionInRoot()
                    val size = c.size
                    val center = Offset(
                        pos.x + size.width / 2f,
                        pos.y + size.height / 2f
                    )
                    onIconMeasured(idx, item.title, center)
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (isSelected) item.selectedIconResId else item.iconResId
                        ),
                        contentDescription = item.title,
                        modifier = Modifier
                            .width(16.dp)
                            .height(17.5.dp),
                        tint = if (isSelected) colors.black else colors.lightGray
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (isSelected) colors.black else colors.lightGray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = colors.black,
                    unselectedIconColor = colors.lightGray,
                    selectedTextColor = colors.black,
                    unselectedTextColor = colors.lightGray,
                    disabledIconColor = Color.Transparent,
                    disabledTextColor = Color.Transparent
                )
            )
        }
    }
}

@Preview
@Composable
private fun MoruBottomBarPreview() {
    MoruBottomBar(
        selectedRoute = Route.Home.route,
        onItemSelected = {},
        onIconMeasured = { _, _, _ -> }
    )
}
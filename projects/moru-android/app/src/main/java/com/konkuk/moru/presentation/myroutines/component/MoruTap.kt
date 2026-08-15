package com.konkuk.moru.presentation.myroutines.component
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme


@Composable
fun MoruTab(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interaction = remember { MutableInteractionSource() }
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .height(32.dp) // [유지]
            .fillMaxWidth()
            .semantics { this.selected = selected }
            .clickable(
                interactionSource = interaction,
                indication = ripple(                      // [변경]
                    bounded = true,                      // 기존과 동일
                    // radius = Dp.Unspecified,          // 필요 시 지정 가능
                    // color = Color.Unspecified         // Material3 RippleTheme 따르게 권장
                ),
                role = Role.Tab,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // [라벨만 fontScale 캡핑]
        CompositionLocalProvider(LocalDensity provides Density(density.density, 1f)) {
            Text(
                text = label,
                style = MORUTheme.typography.time_R_12.copy(
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.Both
                    )
                ),
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip,
                color = if (selected) Color.White else Color.Gray
            )
        }
    }
}
package com.konkuk.moru.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun FocusTypeChip(
    category: String,
    modifier: Modifier = Modifier
) {
    val isValid = category == "집중" || category == "간편"
    if (!isValid) return

    Box(
        modifier = modifier
            .background(
                color = colors.paleLime,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 9.5.dp, vertical = 3.5.dp)
    ) {
        Text(
            text = category,
            style = typography.body_SB_16,
            color = colors.oliveGreen
        )
    }
}


@Preview
@Composable
private fun FocusTypeChipPreview() {
    Column {
        FocusTypeChip(category = "집중")
        FocusTypeChip(category = "간편")
        FocusTypeChip(category = "생활") // 렌더링되지 않음
    }
}

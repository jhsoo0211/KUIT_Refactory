package com.konkuk.moru.core.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme
import com.konkuk.moru.ui.theme.MORUTheme.colors

@Composable
fun TagChip(
    text: String,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(32.dp)
            .background(color = colors.charcoalBlack, shape = RoundedCornerShape(50))
            .padding(horizontal = 13.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#$text",
                style = MORUTheme.typography.time_R_14,
                color = colors.limeGreen
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_tag_remove),
                contentDescription = "Remove Tag",
                tint = colors.oliveGreen,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable(
                        onClick = onRemove,
                        indication = null,
                        interactionSource = null
                    )
            )
        }

    }
}

@Preview
@Composable
private fun TagChipPreview() {
    TagChip(
        text = "태그",
        onRemove = {}
    )
}
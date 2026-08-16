package com.konkuk.moru.core.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme.colors

@Composable
fun AddTagButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(color = colors.charcoalBlack, shape = RoundedCornerShape(50))
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = null
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_createroutine_addtag_arrow),
            contentDescription = "태그 추가 아이콘",
            tint = colors.limeGreen
        )
    }
}

@Preview
@Composable
private fun AddTagButtonPreview() {
    AddTagButton(){}
}
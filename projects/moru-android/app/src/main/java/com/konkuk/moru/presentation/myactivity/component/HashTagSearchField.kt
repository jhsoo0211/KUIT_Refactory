package com.konkuk.moru.presentation.myactivity.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun HashTagSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp)
            .clip(RoundedCornerShape(100.dp))
            .border(1.dp, colors.lightGray, shape = RoundedCornerShape(100.dp))
            .background(Color.White),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.65.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .height(32.dp)
                    .background(colors.black)
                    .padding(horizontal = 13.dp)
            ) {
                Text(
                    text = query,
                    color = colors.limeGreen,
                    style = typography.time_R_14
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = "",
                onValueChange = {
                    if (it.isNotEmpty()) {
                        onQueryChange(query + it)
                    }
                },
                singleLine = true,
                textStyle = typography.time_R_14.copy(color = Color.Transparent),
                modifier = Modifier.weight(1f)
            )
        }
    }
}
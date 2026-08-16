package com.konkuk.moru.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun RoutineSelectItem(
    modifier: Modifier = Modifier,
    text: String = "샤워하기",
    isSelected: Boolean,
    onClick: () -> Unit
) {
    //선택 상태에 따라 바뀌는 색상
    //테두리 색
    val borderColor = if (isSelected) colors.limeGreen else colors.lightGray
    //배경 색
    val backgroundColor = if (isSelected) colors.paleLime else Color.Transparent
    //클릭아이콘 색
    val iconColor = if (isSelected) Color.White else Color.Transparent
    //클릭아이콘 테두리 색
    val iconBorder = if (!isSelected) colors.mediumGray else Color.Transparent

    Box(
        modifier = modifier
            .width(196.dp)
            .height(52.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(10.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 11.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = modifier.width(12.dp))
            Box(
                modifier = Modifier.width(153.dp)
            ) {
                Text(
                    text = text,
                    style = if (text.length > 12) typography.body_SB_14.copy(fontSize = 11.sp)  else typography.body_SB_16,
                    color = colors.charcoalBlack
                )
            }
            Spacer(modifier = Modifier.width(1f.dp))
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) colors.limeGreen else Color.Transparent)
                    .border(width = 1.dp, color = iconBorder, shape = CircleShape)

            ) {
                if (isSelected) {
                    Image(
                        painter = painterResource(id = R.drawable.check_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .width(10.31.dp)
                            .height(7.48.dp)
                            .align(Alignment.Center)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Preview
@Composable
private fun RoutineSelectItemPreview() {
    var isSelected by remember { mutableStateOf(false) }

    RoutineSelectItem(
        text = "샤워하기",
        isSelected = isSelected,
        onClick = { isSelected = !isSelected }
    )
}
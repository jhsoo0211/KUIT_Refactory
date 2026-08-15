package com.konkuk.moru.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun HomeTopAppBar(
    modifier: Modifier = Modifier,

    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(colors.black)
            .padding(top = 7.dp, bottom = 7.13.dp, start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.moru_icon),
            contentDescription = "모루 아이콘 이미지",
            modifier = modifier.size(41.87.dp)
        )
        Spacer(modifier = modifier.width(14.13.dp))
        Text(
            text = "MORU",
            style = typography.desc_M_20.copy(fontWeight = Bold),
            color = colors.paleLime
        )
    }
}

@Preview
@Composable
private fun HomeTopAppBarPreview() {
    HomeTopAppBar()
}
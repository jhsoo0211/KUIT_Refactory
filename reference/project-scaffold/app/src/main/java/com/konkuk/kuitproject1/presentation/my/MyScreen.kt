package com.konkuk.kuitproject1.presentation.my

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.kuitproject1.R
import com.konkuk.kuitproject1.core.component.KuitTitle
import com.konkuk.kuitproject1.core.util.modifier.noRippleClickable

@Composable
fun MyScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        KuitTitle(
            title = "my Screen"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource((R.drawable.ic_my)),
            contentDescription = "my screen image",
            modifier = Modifier
                .size(50.dp)
                .noRippleClickable { }
        )
    }
}

@Preview(
    showBackground = true,
    name = "HomeScreen Preview"
)
@Composable
fun MyScreenPreview() {
    MyScreen(
        modifier = Modifier.fillMaxSize()
    )
}
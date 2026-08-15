package com.konkuk.kuitproject1.presentation.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.konkuk.kuitproject1.R
import com.konkuk.kuitproject1.core.component.KuitTitle
import kotlinx.coroutines.CoroutineScope


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel= hiltViewModel()
) {
    LaunchedEffect(Unit){
        viewModel.getHomeData(1)
        Log.d("Home Screen","get Home Data")
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        KuitTitle(
            title = "Home Screen"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource((R.drawable.ic_home)),
            contentDescription = "my screen image",
            modifier = Modifier
                .size(50.dp)
                .clickable { }
        )
    }

}

@Preview(
    showBackground = true,
    name = "HomeScreen Preview"
)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        modifier = Modifier.fillMaxSize()
    )
}
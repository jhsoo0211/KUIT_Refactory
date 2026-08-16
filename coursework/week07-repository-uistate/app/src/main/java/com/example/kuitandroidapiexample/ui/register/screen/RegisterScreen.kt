package com.example.kuitandroidapiexample.ui.register.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kuitandroidapiexample.App
import com.example.kuitandroidapiexample.ui.home.viewmodel.AnimalViewModel
import com.example.kuitandroidapiexample.ui.model.AnimalType
import com.example.kuitandroidapiexample.ui.register.componet.FindUTextField
import com.example.kuitandroidapiexample.ui.register.componet.TypeSelectContent
import com.example.kuitandroidapiexample.ui.register.uistate.RegisterUiState
import com.example.kuitandroidapiexample.ui.register.viewmodel.RegisterViewModel
import com.example.kuitandroidapiexample.ui.theme.FindUTheme.colors
import com.example.kuitandroidapiexample.ui.theme.FindUTheme.typography
import kotlinx.coroutines.launch


@Composable
fun RegisterScreen(
    padding: PaddingValues,
    navigateToBack: () -> Unit = {},
) {

    val context = LocalContext.current.applicationContext as App
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    val appContainer = context.appContainer

    val viewModel: RegisterViewModel = remember {
        ViewModelProvider(
            viewModelStoreOwner,
            appContainer.registerViewModelFactory
        )[RegisterViewModel::class.java]
    }

    val cScope = rememberCoroutineScope()
    val snackBarHost = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            cScope.launch {
                snackBarHost.showSnackbar(
                    message = "등록이 완료되었습니다.",
                    duration = SnackbarDuration.Short,
                    withDismissAction = true
                )

                navigateToBack()
            }


        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Column(
            modifier = Modifier.matchParentSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "등록하기",
                    modifier = Modifier.align(Alignment.Center),
                    style = typography.semiBold.copy(
                        fontSize = 18.sp
                    )
                )
            }

            FindUTextField(
                modifier = Modifier.padding(top = 21.dp),
                title = "사진 url 입력",
                value = uiState.url
            ) { viewModel.updateUrl(it) }

            FindUTextField(
                modifier = Modifier.padding(top = 15.dp),
                title = "이름 입력",
                value = uiState.reporterName
            ) { viewModel.updateReporterName(it) }

            FindUTextField(
                modifier = Modifier.padding(top = 15.dp),
                title = "주소 입력",
                value = uiState.address
            ) { viewModel.updateAddress(it) }

            FindUTextField(
                modifier = Modifier.padding(top = 15.dp),
                title = "동물 이름",
                value = uiState.name
            ) { viewModel.updateAnimalName(it) }

            TypeSelectContent(
                modifier = Modifier.padding(start = 16.dp, top = 30.dp),
                animalType = uiState.state
            ) { viewModel.updateAnimalType(it) }

        }

        Button(
            modifier = Modifier
                .padding(bottom = 50.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(containerColor = colors.orange),
            shape = RoundedCornerShape(8.dp),
            onClick = {
                viewModel.postAnimal()
            }
        ) {
            Text(
                text = "등록하기", style = typography.semiBold.copy(fontSize = 18.sp)
            )
        }



        SnackbarHost(
            hostState = snackBarHost,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(padding = PaddingValues())
}
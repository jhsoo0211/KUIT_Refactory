package com.konkuk.moru.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.konkuk.moru.presentation.login.component.LoginButton
import com.konkuk.moru.presentation.login.component.LoginTextFieldBasic
import com.konkuk.moru.presentation.login.component.LogoWithTitle
import com.konkuk.moru.presentation.navigation.Route
import com.konkuk.moru.core.validation.AuthInputValidator
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val systemUiController = rememberSystemUiController()
    val backgroundColor = colors.charcoalBlack
    val context = LocalContext.current

    SideEffect {
        systemUiController.setStatusBarColor(
            darkIcons = false, // 상태바 아이콘을 흰색으로 설정
            color = backgroundColor
        )
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isButtonEnabled = remember(email, password) {
        AuthInputValidator.isLoginFormValid(email, password)
    }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.charcoalBlack)
            .padding(horizontal = 32.dp)
            .systemBarsPadding()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus() // 화면을 탭하면 포커스를 해제
                    }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(screenHeight * 0.1825f))
        LogoWithTitle()
        Spacer(modifier = Modifier.height(32.dp))

        LoginTextFieldBasic(
            value = email,
            onValueChange = {
                email = it
                errorMessage = null
            },
            placeholder = "이메일",
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(12.dp))

        LoginTextFieldBasic(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            placeholder = "비밀번호",
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
        ) {
            errorMessage?.let {
                Text(
                    text = it,
                    style = typography.desc_M_12,
                    color = colors.red,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }

        LoginButton(
            onClick = {
                if (isButtonEnabled) {
                    viewModel.login(
                        email = email,
                        password = password,
                        context = context,
                        onSuccess = { isOnboarded -> // [변경] 시그니처 변경
                            // [변경] 서버/로컬이 일치된 상태에서 곧바로 분기
                            if (isOnboarded) {
                                navController.navigate(Route.Main.route) {
                                    popUpTo(Route.Login.route) { inclusive = true }
                                }
                            } else {
                                navController.navigate(Route.Onboarding.route) {
                                    popUpTo(Route.Login.route) { inclusive = true }
                                }
                            }
                        },
                        onFailure = { error ->
                            errorMessage = error.ifBlank { "로그인에 실패했습니다." }
                        }
                    )
                }
            },
            enabled = isButtonEnabled
        )

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = colors.darkGray
        )

        Text(
            text = "회원가입 하기",
            style = typography.desc_M_14,
            color = colors.mediumGray,
            modifier = Modifier
                .padding(top = 12.dp)
                .clickable {
                    navController.navigate(Route.SignUp.route)
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController = navController)
}

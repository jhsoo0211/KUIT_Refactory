package com.example.kuitandroidapiexample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon


import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTarget
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil3.util.CoilUtils.result
import com.example.kuitandroidapiexample.navigation.MainNavHost
import com.example.kuitandroidapiexample.ui.theme.KuitAndroidApiExampleTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KuitAndroidApiExampleTheme {

                var numberState by remember { mutableIntStateOf(value = 0) }
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(Unit) {
                    scope.launch {
                        delay(1000L)
                        numberState += 1

                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState
                        )
                    },
                    floatingActionButton = {
                        DoubleFAB(
                            onClick1 = { loadBlocking() },
                            onClick2 = {
                                scope.launch { nonBlocking() }
                            }
                        )


                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = numberState.toString(),
                            fontSize = 100.sp
                        )

                        MainNavHost(padding = innerPadding)
                    }
                }
            }
        }
    }
}


fun loadBlocking() {
    Thread.sleep(4000L)
}

suspend fun nonBlocking() {
    delay(5000L)

}

@Composable
fun DoubleFAB(
    modifier: Modifier = Modifier,
    onClick1: () -> Unit,
    onClick2: () -> Unit
) {
    ExtendedFloatingActionButton(
        text = { Text(text = "Load Blocking Snack bar") },
        icon = { Icon(Icons.Filled.Check, contentDescription = "") },
        onClick = onClick1

    )
    ExtendedFloatingActionButton(
        text = { Text(text = "Snack bar") },
        icon = { Icon(Icons.Filled.Check, contentDescription = "") },
        onClick = onClick2

    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    //AndroidCoroutineTheme {

}
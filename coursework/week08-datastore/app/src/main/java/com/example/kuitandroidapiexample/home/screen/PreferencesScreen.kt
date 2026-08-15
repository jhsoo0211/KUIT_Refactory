package com.example.kuitandroidapiexample.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.kuitandroidapiexample.util.DataStoreManager
import com.example.kuitandroidapiexample.util.SharedPreferenceManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@Composable
fun PreferencesScreen(modifier: Modifier = Modifier) {


    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    //실습 부분
    /* Column(
         modifier = Modifier.fillMaxSize(),
         horizontalAlignment = Alignment.CenterHorizontally,
         verticalArrangement = Arrangement.Center
     ) {
         TextField(
             value = input,
             onValueChange = { input = it },
             label = { Text("input value") }
         )

         Button(
             onClick = {
                 SharedPreferenceManager.saveValue(
                     context = context,
                     key = "sample_key",
                     value = input
                 )
             }
         ) {
             Text("pref 저장")
         }

         Button(
             onClick = {
                 SharedPreferenceManager.deleteValue(context = context, key = "sample_key")
             }
         ) {
             Text("pref 삭제")
         }

         Button(
             onClick = {
                 result =
                     SharedPreferenceManager.getValue(context = context, key = "sample_key") ?: "없음"
             }
         ) {
             Text("pref 조회")
         }

         Text("결과: $result")
     }*/


    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("input value") }
        )

        Spacer(Modifier.height(8.dp))

        Button(onClick = {
            scope.launch {
                DataStoreManager.saveValue(context, "sample_key", input)
            }
        }) {
            Text("DataStore 저장")
        }

        Spacer(Modifier.height(4.dp))

        Button(onClick = {
            scope.launch {
                DataStoreManager.deleteValue(context, "sample_key")
            }
        }) {
            Text("DataStore 삭제")
        }

        Button(
            onClick = {
                scope.launch {
                    result =
                        DataStoreManager
                            .getValue(context, "sample_key")
                            .first()  // import kotlinx.coroutines.flow.first
                            ?: "없음"
                }
            }
        ) {
            Text("DataStore 조회")
        }

        Spacer(Modifier.height(4.dp))

        Text("결과: $result")
    }
}

@Preview(showBackground = true)
@Composable
private fun PreferencesScreenPreview() {
    PreferencesScreen()
}
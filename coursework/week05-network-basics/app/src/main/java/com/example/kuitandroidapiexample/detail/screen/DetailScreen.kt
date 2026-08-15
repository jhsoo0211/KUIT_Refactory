package com.example.kuitandroidapiexample.detail.screen

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.kuitandroidapiexample.R
import com.example.kuitandroidapiexample.common.TagChip
import com.example.kuitandroidapiexample.home.viewmodel.HomeViewModel
import com.example.kuitandroidapiexample.model.AnimalData.Companion.animalDataList
import com.example.kuitandroidapiexample.ui.theme.FindUTheme.colors
import com.example.kuitandroidapiexample.ui.theme.FindUTheme.typography
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

@Composable
fun DetailScreen(
    padding: PaddingValues,
    id: Int,
    navigateToBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val vm: HomeViewModel = viewModel()
    val animal = vm.detailState.value

    LaunchedEffect(id) { vm.getAnimalDetail(id) }

    if (animal == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Box(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .padding(20.dp)
                        .clickable { navigateToBack() },
                    painter = painterResource(R.drawable.ic_chevron_left),
                    contentDescription = "뒤로 가기",
                    tint = Color.Unspecified
                )
                Button(
                    onClick = { vm.deleteAnimal(animal.id) { success ->
                        if (success) {
                            Toast.makeText(context, "삭제 완료", Toast.LENGTH_SHORT).show()
                            navigateToBack()
                        } else {
                            Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show()
                        }
                    } },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray, // 진한 빨강 (Material Red 700)
                        contentColor = Color.White          // 글씨색 흰색
                    ),
                            modifier = Modifier
                            . align (Alignment.CenterEnd)
                        .padding(end = 20.dp)
                ) {
                    Text("삭제")
                }

            }
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp),
                model = animal.url,
                contentDescription = "동물 사진"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(304.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
        ) {
            Text(
                text = animal.name,
                style = typography.semiBold.copy(fontSize = 24.sp),
                modifier = Modifier.padding(start = 40.dp, top = 42.dp, bottom = 20.dp)
            )

            TagChip(
                modifier = Modifier.padding(start = 40.dp),
                animalType = animal.state
            )
            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .border(width = 1.dp, color = colors.black, shape = RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .shadow(
                        elevation = 10.dp,
                        spotColor = Color(0x1AA0A0A0),
                        ambientColor = Color(0x1AA0A0A0)
                    )
                    .height(89.dp)
                    .padding(horizontal = 40.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "주소",
                    style = typography.semiBold.copy(fontSize = 14.sp, color = colors.orange),
                )
                Text(
                    text = animal.address,
                    style = typography.semiBold.copy(fontSize = 14.sp),
                    modifier = Modifier.align(Alignment.BottomStart)
                )


            }
            Text(
                text = "신고자 : ${animal.reporter ?: "정보 없음"}",
                style = typography.semiBold.copy(fontSize = 14.sp),
                modifier = Modifier.padding(start = 40.dp, top = 21.dp)
            )
        }


    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun DetailScreenPreview() {
    DetailScreen(PaddingValues(), 0)
}
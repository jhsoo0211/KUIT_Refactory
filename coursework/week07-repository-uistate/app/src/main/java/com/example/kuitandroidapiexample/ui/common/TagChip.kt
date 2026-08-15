package com.example.kuitandroidapiexample.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kuitandroidapiexample.ui.model.AnimalType
import com.example.kuitandroidapiexample.ui.theme.FindUTheme.colors
import com.example.kuitandroidapiexample.ui.theme.FindUTheme.typography

@Composable
fun TagChip(
    modifier: Modifier = Modifier,
    animalType: AnimalType
) {
    val containerColor: Color = when (animalType) {
        AnimalType.PROTECT -> colors.lightGreen
        AnimalType.WITNESS -> colors.lightBlue
        AnimalType.MISSING -> colors.lightRed
    }
    val textColor: Color = when (animalType) {
        AnimalType.PROTECT -> colors.green
        AnimalType.WITNESS -> colors.blue
        AnimalType.MISSING -> colors.red
    }

    Box(
        modifier = modifier
            .height(24.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = containerColor)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = animalType.type,
            style = typography.semiBold.copy(fontSize = 12.sp),
            color = textColor
        )
    }
}

@Preview
@Composable
private fun TagChipPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TagChip(
            animalType = AnimalType.PROTECT
        )
        TagChip(
            animalType = AnimalType.MISSING
        )
        TagChip(
            animalType = AnimalType.WITNESS
        )
    }
}
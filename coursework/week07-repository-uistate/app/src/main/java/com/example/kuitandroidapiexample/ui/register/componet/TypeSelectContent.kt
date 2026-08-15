package com.example.kuitandroidapiexample.ui.register.componet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kuitandroidapiexample.ui.model.AnimalType
import com.example.kuitandroidapiexample.ui.theme.FindUTheme.typography

@Composable
fun TypeSelectContent(
    modifier: Modifier = Modifier,
    animalType: AnimalType,
    onSelected: (AnimalType) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        AnimalType.entries.forEach { item ->
            TypeRadioButton(
                selected = (animalType == item),
                type = item.type,
                onSelected = {
                    onSelected(item)
                }
            )
        }
    }
}

@Composable
private fun TypeRadioButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    type: String,
    onSelected: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable { if (!selected) onSelected() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            modifier = Modifier.size(48.dp),
            selected = selected,
            onClick = null
        )
        Text(
            text = type,
            style = typography.semiBold.copy(fontSize = 18.sp)
        )
    }
}

@Preview
@Composable
private fun TypeSelectContentPreview() {
    TypeSelectContent(
        animalType = AnimalType.PROTECT
    ) {}
}
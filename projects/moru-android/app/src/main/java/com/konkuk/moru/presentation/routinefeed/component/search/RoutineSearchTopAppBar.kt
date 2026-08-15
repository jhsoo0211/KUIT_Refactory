package com.konkuk.moru.presentation.routinefeed.component.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RoutineSearchTopAppBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onNavigateBack: () -> Unit,
    placeholderText: String
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        title = {
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MORUTheme.typography.body_SB_16.copy(color = MORUTheme.colors.black),
                singleLine = true,
                cursorBrush = SolidColor(MORUTheme.colors.black),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            text = placeholderText,
                            style = MORUTheme.typography.desc_M_16,
                            color = MORUTheme.colors.lightGray
                        )
                    }
                    innerTextField()
                }
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "뒤로가기",
                    modifier = Modifier.size(32.dp),
                    tint = MORUTheme.colors.black
                )
            }
        },
        actions = {
            IconButton(onClick = onSearch) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_magnifying),
                    contentDescription = "검색",
                    modifier = Modifier.size(24.dp),
                    tint = MORUTheme.colors.black
                )
            }
        }
    )
}

@Preview
@Composable
private fun RoutineSearchTopAppBarPreview() {
    RoutineSearchTopAppBar(
        query = "",
        onQueryChange = {},
        onSearch = {},
        onNavigateBack = {},
        placeholderText = "루틴명을 검색해보세요"
    )
}
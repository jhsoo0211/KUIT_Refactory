package com.konkuk.moru.presentation.myactivity.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.konkuk.moru.R
import com.konkuk.moru.presentation.myactivity.component.BackTitle
import com.konkuk.moru.presentation.myactivity.component.HashTagSearchField
import com.konkuk.moru.presentation.myactivity.component.TagSectionHeader
import com.konkuk.moru.presentation.myactivity.viewmodel.MyActTagViewModel
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography

data class TagDto(
    val id: Int,
    val name: String,
    val isSelected: Boolean = false,
    val serverId: String = ""
)

@Composable
fun ActFabTagScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    vm: MyActTagViewModel = hiltViewModel()
) {
    val allTags by vm.allTags.collectAsState()
    val interestedTags = allTags.filter { it.isSelected }

    var query by remember { mutableStateOf("") }
    val isHashtagMode = query.trim().startsWith("#")

    val selectedTagIds = remember { mutableStateListOf<Int>() }

    val filteredTags = remember(allTags, query) {
        val q = query.trim()
        if (q.isEmpty()) allTags
        else allTags.filter { it.name.contains(q, ignoreCase = true) }
    }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        vm.loadAllTagsAndFavorites()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Spacer(modifier = Modifier.height(14.dp))
                        BackTitle(title = "내 관심 태그", navController = navController)
                        Spacer(modifier = Modifier.height(14.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(colors.lightGray)
                        )
                    }
                },
                bottomBar = {
                    val hasSelection = selectedTagIds.isNotEmpty()
                    Box(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                            .height(60.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (hasSelection) Color.Black else colors.mediumGray)
                            .clickable(enabled = hasSelection) {
                                vm.submitFavoriteTags(selectedTagIds.toList())

                                selectedTagIds.clear()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "관심태그 추가",
                            color = if (hasSelection) colors.paleLime else Color.White,
                            style = typography.body_SB_16
                        )
                    }
                }
            ) { innerPadding ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .border(1.dp, colors.lightGray, shape = RoundedCornerShape(24.dp))
                            .background(Color(0xFFF5F6F8)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_magnifying),
                                contentDescription = "Search Icon",
                                modifier = Modifier
                                    .size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                            BasicTextField(
                                value = query,
                                onValueChange = { query = it },
                                singleLine = true,
                                textStyle = typography.time_R_14.copy(color = Color.Black),
                                modifier = Modifier.weight(1f),
                                decorationBox = { innerTextField ->
                                    if (query.isEmpty()) {
                                        Text(
                                            text = "태그를 검색해 보세요.",
                                            style = typography.time_R_14,
                                            color = colors.mediumGray
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(25.dp))
                    TagSectionHeader("관심태그")

                    Spacer(modifier = Modifier.height(24.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        interestedTags.forEach { tag ->
                            TagChip(
                                text = tag.name,
                                selected = false,
                                onClick = {
                                    vm.removeFavoriteTag(tag.id)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(42.dp))
                    TagSectionHeader("전체태그")

                    Spacer(modifier = Modifier.height(24.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        filteredTags.forEach { tag ->
                            val isSelected = selectedTagIds.contains(tag.id)
                            TagChip(
                                text = tag.name,
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) {
                                        selectedTagIds.remove(tag.id)
                                    } else {
                                        selectedTagIds.add(tag.id)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (isHashtagMode) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 60.dp, start = 16.dp, end = 16.dp)
            ) {
                HashTagSearchField(query, onQueryChange = { query = it })
                Spacer(modifier = Modifier.height(17.dp))

                val hashtagFilteredTags = allTags.filter {
                    it.name.contains(query.trim(), ignoreCase = true)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.65.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    hashtagFilteredTags.forEach { tag ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .height(32.dp)
                                .clip(RoundedCornerShape(100.dp))
                                .background(Color.Black)
                                .clickable {
                                    vm.submitFavoriteTags(listOf(tag.id))
                                    query = ""
                                }
                                .padding(horizontal = 13.dp)
                        ) {
                            Text(
                                text = tag.name,
                                color = colors.limeGreen,
                                style = typography.time_R_14
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TagChip(
    text: String,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val backgroundColor = if (selected) colors.paleLime else Color(0xFFF5F6F8)
    val textColor = if (selected) colors.oliveGreen else colors.mediumGray

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(140.dp))
            .then(
                if (selected) Modifier.border(
                    1.dp,
                    colors.oliveGreen,
                    RoundedCornerShape(140.dp)
                ) else Modifier
            )
            .background(backgroundColor)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 13.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = typography.time_R_14,
            color = textColor
        )
    }
}

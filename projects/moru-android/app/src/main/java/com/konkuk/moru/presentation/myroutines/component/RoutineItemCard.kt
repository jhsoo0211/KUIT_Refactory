package com.konkuk.moru.presentation.myroutines.component

import android.R.attr.contentDescription
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.konkuk.moru.R
import com.konkuk.moru.core.component.Switch.RoutineSimpleFocusSwitch
import com.konkuk.moru.core.component.chip.MoruChip
import com.konkuk.moru.core.component.routinedetail.RoutineDescriptionField
import com.konkuk.moru.core.component.routinedetail.ShowUserCheckbox
import com.konkuk.moru.presentation.routinefeed.component.modale.CenteredInfoDialog
import com.konkuk.moru.presentation.routinefeed.component.modale.CustomDialog
import com.konkuk.moru.ui.theme.MORUTheme
import com.konkuk.moru.ui.theme.moruFontBold
import kotlinx.coroutines.delay
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

@Composable
fun RoutineItemCard(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    imageUri: Uri? = null,
    title: String,
    isEditMode: Boolean,
    onDelete: () -> Unit,
    description: String,
    category: String,
    onDescriptionChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onImageClick: () -> Unit,
    isUserVisible: Boolean,                 // [추가]
    onUserVisibleChange: (Boolean) -> Unit, // [추가]
) {
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showDeleteCompleteDialog by remember { mutableStateOf(false) }
    var isUserChecked by remember { mutableStateOf(false) }

    if (showDeleteConfirmDialog) {
        CustomDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            onConfirmation = {
                showDeleteConfirmDialog = false
                showDeleteCompleteDialog = true
                onDelete()
            },
            content = {
                Text(
                    text = "루틴을 삭제하시겠습니까?",
                    style = MORUTheme.typography.title_B_20,
                    textAlign = TextAlign.Center
                )
            }
        )
    }
    if (showDeleteCompleteDialog) {
        CenteredInfoDialog(
            onDismissRequest = { showDeleteCompleteDialog = false },
            content = {
                Text(
                    text = "삭제되었습니다!",
                    color = Color.LightGray,
                    style = MORUTheme.typography.desc_M_14
                )
            }
        )
        LaunchedEffect(Unit) {
            delay(1500)
            showDeleteCompleteDialog = false
        }
    }
    Row(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = imageUri ?: imageUrl,
            contentDescription = "루틴 대표 이미지",
            modifier = Modifier
                .width(105.dp)
                .height(140.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable(
                    enabled = isEditMode,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onImageClick() },
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_routine_card_basic),
            error = painterResource(id = R.drawable.ic_routine_card_basic)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontFamily = moruFontBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.weight(1f, fill = false),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!isEditMode) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { showDeleteConfirmDialog = true },
                        tint = MORUTheme.colors.mediumGray,
                        painter = painterResource(R.drawable.ic_trash),
                        contentDescription = " 쓰레기통"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                ShowUserCheckbox(
                    showUser = isUserVisible,                                  // [변경]
                    onClick = { if (isEditMode) onUserVisibleChange(!isUserVisible) } // [변경]
                )

                if (!isEditMode) {
                    MoruChip(
                        modifier = Modifier.height(28.dp),
                        text = category,
                        onClick = {},
                        isSelected = true,
                        selectedBackgroundColor = Color(0xFFEBFFC0),
                        selectedContentColor = Color(0xFF8CCD00),
                        unselectedBackgroundColor = Color.Transparent,
                        unselectedContentColor = Color.Transparent
                    )

                } else {
                    RoutineSimpleFocusSwitch(
                        checked = category == "집중",
                        onClick = { isChecked ->
                            onCategoryChange(if (isChecked) "집중" else "간편")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isEditMode) {
                RoutineDescriptionField(
                    value = description,
                    placeholder = "설명을 입력해주세요.",
                    onValueChange = onDescriptionChange
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((74.dp))
                        .background(
                            color = MORUTheme.colors.veryLightGray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(10.dp)
                ) {

                    Text(
                        text = if (description.isNotBlank()) description else "설명을 입력해주세요.",
                        modifier = Modifier.fillMaxWidth(),
                        style = MORUTheme.typography.time_R_14.copy(
                            color = if (description.isNotBlank()) Color.Black else Color.Gray
                        ),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun RoutineItemCardPreview() {
    RoutineItemCard(
        title = "PIG",
        category = "집중",
        description = "설명",
        isEditMode = true,
        imageUrl = null,
        imageUri = null,
        onDelete = {},
        onCategoryChange = { _ -> },
        onDescriptionChange = { _ -> },
        onImageClick = {},
        isUserVisible = true,                 // [추가]
        onUserVisibleChange = { _ -> }
    )
}
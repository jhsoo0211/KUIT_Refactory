package com.konkuk.moru.presentation.myroutines.component


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konkuk.moru.R
import com.konkuk.moru.core.component.button.MoruButton
import com.konkuk.moru.ui.theme.MORUTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.TextStyle as JavaTextStyle
import java.util.Locale
import kotlin.math.abs

enum class RepeatMode { NONE, EVERYDAY, WEEKDAYS, WEEKENDS }

@Composable
fun TimePickerSheetContent(
    initialTime: LocalTime? = null,
    initialDays: Set<DayOfWeek> = emptySet(),
    initialAlarm: Boolean = true,
    onConfirm: (LocalTime, Set<DayOfWeek>, Boolean) -> Unit

) {
    // 1) 초기값 → AM/PM + 12시간 변환
    val initAmPm = when {
        initialTime == null -> "오후" // 기본
        initialTime.hour == 0 || initialTime.hour < 12 -> "오전"
        else -> "오후"
    }
    val initHour12 = when {
        initialTime == null -> 2
        initialTime.hour == 0 -> 12
        initialTime.hour in 1..12 -> initialTime.hour
        else -> initialTime.hour - 12
    }
    val initMinute = initialTime?.minute ?: 1

    // 2) remember에 초기값 바인딩 (초기값이 바뀌면 재설정 되도록 key 지정)
    var selectedAmPm by remember(initialTime) { mutableStateOf(initAmPm) }
    var selectedHour by remember(initialTime) { mutableStateOf(initHour12) }
    var selectedMinute by remember(initialTime) { mutableStateOf(initMinute) }
    var selectedDays by remember(initialDays) { mutableStateOf(initialDays) }
    var repeatMode by remember { mutableStateOf(RepeatMode.NONE) }
    var isAlarmOn by remember(initialAlarm) { mutableStateOf(initialAlarm) }

    // 2. 상태를 기본값으로 되돌리는 초기화 함수
    fun resetToDefaults() {
        selectedAmPm = initAmPm
        selectedHour = initHour12
        selectedMinute = initMinute
        selectedDays = initialDays
        repeatMode = RepeatMode.NONE
        isAlarmOn = initialAlarm
    }

    LaunchedEffect(repeatMode) {
        selectedDays = when (repeatMode) {
            RepeatMode.EVERYDAY -> DayOfWeek.values().toSet()
            RepeatMode.WEEKDAYS -> setOf(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY
            )

            RepeatMode.WEEKENDS -> setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
            RepeatMode.NONE -> if (selectedDays.size > 1) emptySet() else selectedDays
        }
    }
    val onDayClick = { day: DayOfWeek ->
        repeatMode = RepeatMode.NONE
        selectedDays = if (selectedDays.contains(day)) selectedDays - day else selectedDays + day
    }

    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("반복", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        val itemHeight = 40.dp
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight * 3),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight)
                    .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(8.dp))
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                WheelPicker(
                    items = listOf("오전", "오후"),
                    itemHeight = itemHeight,
                    initialItem = selectedAmPm,
                    onSelectionChanged = { selectedAmPm = it }
                )
                WheelPicker(
                    items = (1..12).map { it.toString().padStart(2, '0') },
                    itemHeight = itemHeight,
                    initialItem = selectedHour.toString().padStart(2, '0'),
                    onSelectionChanged = { selectedHour = it.toInt() }
                )
                Text(
                    ":",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                WheelPicker(
                    items = (0..59).map { it.toString().padStart(2, '0') },
                    itemHeight = itemHeight,
                    initialItem = selectedMinute.toString().padStart(2, '0'),
                    onSelectionChanged = { selectedMinute = it.toInt() }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        RepeatModeSelector(selectedMode = repeatMode, onModeSelected = { repeatMode = it })
        Spacer(modifier = Modifier.height(8.dp))
        DayOfWeekSelector(selectedDays = selectedDays, onDayClick = onDayClick)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp)
                .clickable { isAlarmOn = !isAlarmOn },
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon =
                if (isAlarmOn) Icons.Outlined.CheckBox else Icons.Outlined.CheckBoxOutlineBlank
            Icon(
                imageVector = icon,
                contentDescription = "알림 받기",
                tint = if (isAlarmOn) Color.Black else Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("알림 받기", color = Color.Black, fontWeight = FontWeight(400))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MoruButton(
                text = "초기화",
                onClick = { resetToDefaults() },
                backgroundColor = MORUTheme.colors.lightGray,
                contentColor = Color(0xFF595959),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .wrapContentWidth()
                    .height(52.dp),
                iconContent = {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = R.drawable.ic_reset),
                        contentDescription = "초기화"
                    )
                }
            )
            MoruButton(
                text = "확인",
                onClick = {
                    val hour24 = when {
                        selectedAmPm == "오후" && selectedHour != 12 -> selectedHour + 12
                        selectedAmPm == "오전" && selectedHour == 12 -> 0
                        else -> selectedHour
                    }
                    onConfirm(LocalTime.of(hour24, selectedMinute), selectedDays, isAlarmOn)
                },
                backgroundColor = MORUTheme.colors.limeGreen,
                contentColor = Color.White,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            )
        }
    }
}

@Composable
private fun WheelPicker(
    items: List<String>,
    modifier: Modifier = Modifier,
    itemHeight: Dp,
    initialItem: String,
    onSelectionChanged: (String) -> Unit
) {
    val itemCount = Int.MAX_VALUE
    val actualItemCount = items.size
    val centralIndex = itemCount / 2
    val initialIndex =
        centralIndex + (items.indexOf(initialItem).takeIf { it != -1 }
            ?: 0) - (centralIndex % actualItemCount)

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior: FlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val derivedCentralItemIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty()) 0
            else {
                val viewportCenter =
                    (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                layoutInfo.visibleItemsInfo
                    .minByOrNull { abs(it.offset + it.size / 2 - viewportCenter) }?.index ?: 0
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { derivedCentralItemIndex }
            .distinctUntilChanged()
            .collect { index -> onSelectionChanged(items[index % actualItemCount]) }
    }

    LazyColumn(
        state = listState,
        flingBehavior = flingBehavior,
        modifier = modifier
            .height(itemHeight * 3)
            .width(70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = itemHeight)
    ) {
        items(count = itemCount) { index ->
            val isSelected = index == derivedCentralItemIndex
            val scale = if (isSelected) 1.2f else 0.8f
            val alpha = if (isSelected) 1.0f else 0.4f

            Box(
                modifier = Modifier.height(itemHeight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = items[index % actualItemCount],
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .scale(scale)
                        .alpha(alpha)
                )
            }
        }
    }
}

@Composable
private fun RepeatModeSelector(selectedMode: RepeatMode, onModeSelected: (RepeatMode) -> Unit) {
    val modes = mapOf(
        "매일" to RepeatMode.EVERYDAY,
        "평일만" to RepeatMode.WEEKDAYS,
        "주말만" to RepeatMode.WEEKENDS
    )
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        modes.forEach { (text, mode) ->
            val isSelected = selectedMode == mode
            Text(
                text = text,
                color = if (isSelected) Color.Black else Color.Gray,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.clickable { onModeSelected(mode) }
            )
        }
    }
}


@Composable
private fun DayOfWeekSelector(selectedDays: Set<DayOfWeek>, onDayClick: (DayOfWeek) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        DayOfWeek.values().forEach { day ->
            val dayShortName = day.getDisplayName(JavaTextStyle.SHORT, Locale.KOREAN)
            val isSelected = selectedDays.contains(day)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) MORUTheme.colors.limeGreen else Color.Transparent)
                    .clickable { onDayClick(day) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dayShortName,
                    color = if (isSelected) Color.White else Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TimeSetPreview() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    MORUTheme {
        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            containerColor = Color.White
        ) {
            TimePickerSheetContent(
                onConfirm = { _, _, _ -> }
            )
        }
    }
}
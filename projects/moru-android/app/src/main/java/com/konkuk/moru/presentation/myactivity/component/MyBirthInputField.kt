package com.konkuk.moru.presentation.myactivity.component

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import com.konkuk.moru.R
import com.konkuk.moru.ui.theme.MORUTheme.colors
import com.konkuk.moru.ui.theme.MORUTheme.typography
import java.util.Calendar

@Composable
fun MyActBirthInputField(
    value: String,                      // 화면 표기 값: "yyyy.MM.dd" (빈 문자열 허용)
    onValueChange: (String) -> Unit,    // 사용자가 날짜 고르면 "yyyy.MM.dd"로 콜백
    modifier: Modifier = Modifier,
    placeholder: String = "yyyy.MM.dd"
) {
    val context = LocalContext.current

    // value가 점/하이픈 어떤 포맷이든 초기선택값으로 파싱 (실패 시 오늘 날짜)
    val (initYear, initMonthZeroBased, initDay) = remember(value) {
        parseYMDToPickerDefaults(value) ?: todayPickerDefaults()
    }

    Box(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth()) {
            Text(
                text = "생년월일",
                color = colors.black,
                style = typography.body_SB_16,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.lightGray, RoundedCornerShape(4.dp))
                    .clip(RoundedCornerShape(10.5.dp))
                    .height(45.dp)
                    .clickable {
                        DatePickerDialog(
                            context,
                            { _, y, m, d ->
                                val formatted = "%04d.%02d.%02d".format(y, m + 1, d) // 항상 점 포맷
                                onValueChange(formatted)
                            },
                            initYear,
                            initMonthZeroBased, // DatePickerDialog는 0-based month
                            initDay
                        ).show()
                    }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val showText = value.ifBlank { placeholder }
                    val showColor = if (value.isBlank()) colors.mediumGray else colors.black

                    Text(text = showText, style = typography.desc_M_14, color = showColor)
                    Image(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = "달력",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

/** "yyyy.MM.dd" 또는 "yyyy-MM-dd"를 받아 DatePicker 초기값(연, 0-based 월, 일)로 변환 */
private fun parseYMDToPickerDefaults(s: String?): Triple<Int, Int, Int>? {
    val t = s?.trim().orEmpty()
    if (t.isEmpty()) return null
    val sep = when {
        t.contains('.') -> '.'
        t.contains('-') -> '-'
        else -> return null
    }
    val parts = t.split(sep)
    if (parts.size != 3) return null
    val y = parts[0].toIntOrNull() ?: return null
    val m = parts[1].toIntOrNull() ?: return null
    val d = parts[2].toIntOrNull() ?: return null
    if (m !in 1..12 || d !in 1..31) return null
    return Triple(y, m - 1, d) // DatePickerDialog는 0-based month
}

private fun todayPickerDefaults(): Triple<Int, Int, Int> {
    val c = Calendar.getInstance()
    return Triple(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
}

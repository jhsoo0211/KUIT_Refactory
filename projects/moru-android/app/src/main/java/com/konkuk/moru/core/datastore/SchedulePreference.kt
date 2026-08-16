package com.konkuk.moru.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.DayOfWeek
import java.time.LocalTime
import kotlinx.coroutines.flow.first
import kotlin.collections.map
import kotlin.collections.mapNotNull
import kotlin.collections.toSet

private val Context.scheduleDataStore: DataStore<Preferences> by preferencesDataStore(name = "schedule_prefs")

object SchedulePreference {
    
    // 스케줄 정보를 담는 데이터 클래스
    @Serializable
    data class ScheduleInfo(
        val routineId: String,
        val scheduledDays: List<String>, // DayOfWeek를 String으로 저장 (MON, TUE, WED, ...)
        val scheduledTime: String?       // LocalTime을 String으로 저장 (HH:mm)
    )
    
    // DayOfWeek를 String으로 변환
    fun dayOfWeeksToStrings(days: Set<DayOfWeek>): List<String> {
        val result = mutableListOf<String>()
        for (day in days) {
            val dayString = when (day) {
                DayOfWeek.MONDAY -> "MON"
                DayOfWeek.TUESDAY -> "TUE"
                DayOfWeek.WEDNESDAY -> "WED"
                DayOfWeek.THURSDAY -> "THU"
                DayOfWeek.FRIDAY -> "FRI"
                DayOfWeek.SATURDAY -> "SAT"
                DayOfWeek.SUNDAY -> "SUN"
            }
            result.add(dayString)
        }
        return result
    }
    
    // String을 DayOfWeek로 변환
    fun stringsToDayOfWeeks(dayStrings: List<String>): Set<DayOfWeek> {
        val result = mutableSetOf<DayOfWeek>()
        for (dayString in dayStrings) {
            val dayOfWeek = when (dayString.uppercase()) {
                "MON" -> DayOfWeek.MONDAY
                "TUE" -> DayOfWeek.TUESDAY
                "WED" -> DayOfWeek.WEDNESDAY
                "THU" -> DayOfWeek.THURSDAY
                "FRI" -> DayOfWeek.FRIDAY
                "SAT" -> DayOfWeek.SATURDAY
                "SUN" -> DayOfWeek.SUNDAY
                else -> null
            }
            if (dayOfWeek != null) {
                result.add(dayOfWeek)
            }
        }
        return result
    }
    
    // LocalTime을 String으로 변환
    fun localTimeToString(time: LocalTime?): String? = time?.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
    
    // String을 LocalTime으로 변환
    fun stringToLocalTime(timeString: String?): LocalTime? = timeString?.let { timeStr ->
        try {
            LocalTime.parse(timeStr, java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
        } catch (e: Exception) {
            null
        }
    }
    
    // 루틴별 스케줄 정보를 저장하는 키
    private val SCHEDULE_DATA_KEY = stringPreferencesKey("schedule_data")
    
    // 스케줄 정보 저장
    suspend fun saveSchedule(context: Context, scheduleInfo: ScheduleInfo) {
        val allSchedules = getSchedules(context).toMutableList()
        
        // 기존 스케줄 정보가 있으면 업데이트, 없으면 추가
        val existingIndex = allSchedules.indexOfFirst { it.routineId == scheduleInfo.routineId }
        if (existingIndex >= 0) {
            allSchedules[existingIndex] = scheduleInfo
        } else {
            allSchedules.add(scheduleInfo)
        }
        
        context.scheduleDataStore.edit { prefs ->
            prefs[SCHEDULE_DATA_KEY] = Json.encodeToString(allSchedules)
        }
    }
    
    // 특정 루틴의 스케줄 정보 가져오기
    suspend fun getSchedule(context: Context, routineId: String): ScheduleInfo? {
        val allSchedules = getSchedules(context)
        return allSchedules.find { it.routineId == routineId }
    }
    
    // 모든 스케줄 정보 가져오기
    suspend fun getSchedules(context: Context): List<ScheduleInfo> {
        return try {
            var result: List<ScheduleInfo> = emptyList()
            context.scheduleDataStore.data.collect { prefs ->
                val json = prefs[SCHEDULE_DATA_KEY] ?: "[]"
                result = Json.decodeFromString<List<ScheduleInfo>>(json)
            }
            result
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // 스케줄 정보 삭제
    suspend fun deleteSchedule(context: Context, routineId: String) {
        val allSchedules = getSchedules(context).filter { it.routineId != routineId }
        context.scheduleDataStore.edit { prefs ->
            prefs[SCHEDULE_DATA_KEY] = Json.encodeToString(allSchedules)
        }
    }
} 
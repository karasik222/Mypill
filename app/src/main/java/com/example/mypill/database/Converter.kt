package com.example.mypill.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converters {

    // Конвертация Long (timestamp) в Date
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    // Конвертация Date в Long (timestamp)
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // Конвертация списка дат (List<Date>) в строку
    @TypeConverter
    fun fromConsumptionTimes(value: String?): List<Date> {
        val listType = object : TypeToken<List<Date>>() {}.type
        return value?.let { Gson().fromJson(it, listType) } ?: emptyList()
    }

    // Конвертация строки в список дат (List<Date>)
    @TypeConverter
    fun toConsumptionTimes(value: List<Date>?): String {
        return Gson().toJson(value)
    }

}

package com.example.mypill.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val isTaken: Boolean,
    val dosage: Double,
    val repetition: String,
    val startDate: Date,
    val endDate: Date,
    val consumptionTimes: List<Date> // Список времен приема

)

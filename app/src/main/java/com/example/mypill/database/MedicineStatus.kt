package com.example.mypill.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicine_status")
data class MedicineStatus(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val medicineId: Int,
    val date: String,
    val time: String,
    val isTaken: Boolean
)

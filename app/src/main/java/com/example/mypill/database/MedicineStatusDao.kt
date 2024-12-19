package com.example.mypill.database

import androidx.room.*

@Dao
interface MedicineStatusDao {
    @Query("SELECT * FROM medicine_status WHERE medicineId = :medicineId AND date = :date AND time = :time LIMIT 1")
    suspend fun getMedicineStatus(medicineId: Int, date: String, time: String): MedicineStatus?

    @Insert
    suspend fun insertMedicineStatus(status: MedicineStatus)

    @Delete
    suspend fun deleteMedicineStatus(status: MedicineStatus)

    @Query("SELECT * FROM medicine_status")
    suspend fun getAllStatuses(): List<MedicineStatus>

    @Update
    suspend fun updateMedicineStatus(medicineStatus: MedicineStatus)

}




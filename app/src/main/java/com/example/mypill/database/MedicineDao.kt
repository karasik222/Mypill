package com.example.mypill.database

import androidx.room.*

@Dao
interface MedicineDao {
    @Insert
    suspend fun insertMedicine(medicine: Medicine)

    @Update
    suspend fun updateMedicine(medicine: Medicine)

    @Delete
    suspend fun deleteMedicine(medicine: Medicine)

    @Query("SELECT * FROM medicines")
    suspend fun getAllMedicines(): List<Medicine>





}

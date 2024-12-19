package com.example.mypill
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateListOf
import com.example.mypill.database.AppDatabase
import com.example.mypill.database.Medicine
import com.example.mypill.database.MedicineStatus
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MedicineViewModel(application: Application) : AndroidViewModel(application) {

    private val medicineDao = AppDatabase.getDatabase(application).medicineDao()
    private val medicineStatusDao = AppDatabase.getDatabase(application).medicineStatusDao()

    private val _medicines = mutableStateListOf<Medicine>()
    val medicines: List<Medicine> get() = _medicines

    private val _medicineStatuses = mutableStateListOf<MedicineStatus>()
    val medicineStatuses: List<MedicineStatus> get() = _medicineStatuses

    init {
        loadMedicines()
        loadMedicineStatuses()
    }

    private fun loadMedicines() {
        viewModelScope.launch {
            try {
                val medicinesFromDb = medicineDao.getAllMedicines()
                _medicines.clear()
                _medicines.addAll(medicinesFromDb)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateMedicine(medicine: Medicine) {
        viewModelScope.launch {
            try {
                medicineDao.updateMedicine(medicine)
                loadMedicines()
                scheduleRemindersForMedicine(getApplication(), medicine)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getMedicineById(medicineId: Int?): Medicine? {
        return _medicines.find { it.id == medicineId }
    }

    fun deleteMedicine(medicine: Medicine) {
        viewModelScope.launch {
            try {
                medicineDao.deleteMedicine(medicine)
                _medicines.remove(medicine)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadMedicineStatuses() {
        viewModelScope.launch {
            try {
                val statusesFromDb = medicineStatusDao.getAllStatuses()
                _medicineStatuses.clear()
                _medicineStatuses.addAll(statusesFromDb)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveMedicine(medicine: Medicine) {
        viewModelScope.launch {
            try {
                medicineDao.insertMedicine(medicine)
                _medicines.add(medicine)
                scheduleRemindersForMedicine(getApplication(), medicine)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun scheduleRemindersForMedicine(context: Application, medicine: Medicine) {
        val calendar = Calendar.getInstance().apply { time = medicine.startDate }
        val currentTime = Calendar.getInstance().timeInMillis

        medicine.consumptionTimes.forEach { time ->
            val timeInMillis = time.time

            if (timeInMillis > currentTime) {
                val delayMillis = timeInMillis - currentTime

                val inputData = Data.Builder()
                    .putString("medicineName", medicine.name)
                    .putString("time", SimpleDateFormat("HH:mm").format(time))
                    .build()

                val reminderRequest = OneTimeWorkRequest.Builder(MedicineReminderWorker::class.java)
                    .setInputData(inputData)
                    .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                    .build()

                WorkManager.getInstance(context).enqueue(reminderRequest)
            }
        }
    }


    fun isMedicineTaken(medicine: Medicine, date: String, time: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val status = medicineStatusDao.getMedicineStatus(medicine.id, date, time)
            callback(status?.isTaken ?: false)
        }
    }

    fun toggleMedicineStatus(medicine: Medicine, date: String, time: String) {
        viewModelScope.launch {
            val status = medicineStatusDao.getMedicineStatus(medicine.id, date, time)
            if (status == null) {
                val newStatus = MedicineStatus(medicineId = medicine.id, date = date, time = time, isTaken = true)
                medicineStatusDao.insertMedicineStatus(newStatus)
            } else {
                val updatedStatus = status.copy(isTaken = !status.isTaken)
                medicineStatusDao.updateMedicineStatus(updatedStatus)
            }
            loadMedicineStatuses()
        }
    }
}

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Data
import java.util.concurrent.TimeUnit
import android.content.Context
import com.example.mypill.MedicineReminderWorker

fun scheduleMedicineReminder(context: Context, medicineName: String, time: String, delayMillis: Long) {

    val inputData = Data.Builder()
        .putString("medicineName", medicineName)
        .putString("time", time)
        .build()

    val reminderRequest = OneTimeWorkRequest.Builder(MedicineReminderWorker::class.java)
        .setInputData(inputData)
        .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueue(reminderRequest)
}

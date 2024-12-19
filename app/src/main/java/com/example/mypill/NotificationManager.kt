import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.Manifest
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.mypill.MainActivity
import com.example.mypill.R

fun sendMedicineNotification(context: Context, medicineName: String, time: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)

        if (permissionCheck == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            createAndShowNotification(context, medicineName, time)
        } else {
            return
        }
    } else {
        createAndShowNotification(context, medicineName, time)
    }
}

fun createAndShowNotification(context: Context, medicineName: String, time: String) {
    val notificationId = 1
    val channelId = "medicine_reminder_channel"
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent: PendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )


}

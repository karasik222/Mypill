package com.example.mypill

import android.content.Context
import android.content.Intent
import android.app.PendingIntent

import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MedicineReminderWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val medicineName = inputData.getString("medicineName") ?: return Result.failure()
        val time = inputData.getString("time") ?: return Result.failure()
        sendMedicineNotification(applicationContext, medicineName, time)

        return Result.success()
    }

    private fun sendMedicineNotification(context: Context, medicineName: String, time: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "medicine_reminder_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Время для приема лекарства!")
            .setContentText("Не забудьте принять: $medicineName в $time.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, notification)
    }
}


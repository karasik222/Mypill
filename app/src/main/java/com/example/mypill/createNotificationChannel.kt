package com.example.mypill

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.content.Context

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "medicine_reminder_channel"
        val channelName = "Medicine Reminder"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.description = "Channel for medicine reminders"

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }
}

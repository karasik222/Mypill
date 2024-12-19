package com.example.mypill

import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.app.NotificationCompat
import android.content.Intent
import android.app.PendingIntent
import java.util.Calendar

class MainActivity : ComponentActivity() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), PERMISSION_REQUEST_CODE)
        } else {
            Toast.makeText(this, "Permission already granted!", Toast.LENGTH_SHORT).show()
            createNotificationChannel()
            sendNotification()
        }

        setContent {
            val viewModel: MedicineViewModel = viewModel(
                factory = MedicineViewModelFactory(application)
            )
            MaterialTheme {
                val navController = rememberNavController()

                val selectedDate = remember { mutableStateOf(Calendar.getInstance().time) }

                NavHost(navController, startDestination = "medicine_list") {
                    composable("medicine_list") {
                        MedicineListScreen(
                            onAddClick = {
                                navController.navigate("add_medicine")
                            },
                            onEditClick = { medicine ->
                                navController.navigate("edit_medicine/${medicine.id}")
                            },
                            onDeleteClick = { medicine ->
                                viewModel.deleteMedicine(medicine)
                            },
                            viewModel = viewModel,
                            selectedDate = selectedDate.value
                        )
                    }
                    composable("add_medicine") {
                        AddMedicineScreen(
                            onSave = { medicine ->
                                viewModel.saveMedicine(medicine)
                                navController.popBackStack()
                            },
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("edit_medicine/{medicineId}") { backStackEntry ->
                        val medicineId = backStackEntry.arguments?.getString("medicineId")?.toIntOrNull()
                        val selectedMedicine = viewModel.getMedicineById(medicineId)
                        if (selectedMedicine != null) {
                            EditMedicineScreen(
                                viewModel = viewModel,
                                medicine = selectedMedicine,
                                onSaveClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "medicine_reminder_channel"
            val channelName = "Medicine Reminder"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = "Channel for medicine reminders"

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }


    private fun sendNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }


        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(this, "medicine_reminder_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Время для приема лекарства!")
            .setContentText("Не забудьте принять лекарство.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                createNotificationChannel()
                sendNotification()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

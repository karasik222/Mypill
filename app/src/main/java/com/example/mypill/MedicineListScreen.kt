package com.example.mypill
import kotlin.math.abs
import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mypill.database.Medicine
import java.text.SimpleDateFormat
import androidx.compose.ui.graphics.Color
import java.util.*
import androidx.compose.foundation.shape.RoundedCornerShape

import java.util.Locale

@Composable
fun MedicineListScreen(
    onAddClick: () -> Unit,
    onEditClick: (Medicine) -> Unit,
    onDeleteClick: (Medicine) -> Unit,
    selectedDate: Date,
    viewModel: MedicineViewModel
) {
    var selectedDate by remember { mutableStateOf(Date()) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var medicineToDelete by remember { mutableStateOf<Medicine?>(null) }
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Выбранная дата: ", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = SimpleDateFormat("dd/MM/yyyy").format(selectedDate),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    val calendar = Calendar.getInstance()
                    calendar.time = selectedDate
                    val datePickerDialog = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            val selected = Calendar.getInstance().apply {
                                set(year, month, dayOfMonth)
                            }.time
                            selectedDate = selected
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                    datePickerDialog.show()
                },
                modifier = Modifier.height(56.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Выбрать дату"
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(viewModel.medicines) { medicine ->
                if (isMedicineDueOnDate(medicine, selectedDate)) {
                    val sortedTimes = medicine.consumptionTimes.sortedBy { it.time }

                    sortedTimes.forEach { time ->
                        val formattedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate)
                        val formattedTime =
                            SimpleDateFormat("HH:mm", Locale.getDefault()).format(time)

                        val isTakenState = remember { mutableStateOf(false) }

                        LaunchedEffect(medicine, selectedDate, time) {
                            viewModel.isMedicineTaken(
                                medicine,
                                formattedDate,
                                formattedTime
                            ) { isTaken ->
                                isTakenState.value = isTaken
                            }
                        }

                        MedicineCard(
                            medicine = medicine,
                            time = time,
                            selectedDate = selectedDate,
                            onEditClick = onEditClick,
                            onDeleteClick = {
                                medicineToDelete = medicine
                                showDeleteDialog = true
                            },
                            onStatusChange = { updatedMedicine, date, time ->
                                val formattedDate =
                                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
                                val formattedTime =
                                    SimpleDateFormat("HH:mm", Locale.getDefault()).format(time)
                                viewModel.toggleMedicineStatus(
                                    updatedMedicine,
                                    formattedDate,
                                    formattedTime
                                )
                                isTakenState.value = !isTakenState.value
                            },
                            isTaken = isTakenState.value
                        )
                    }
                }
            }
        }

        Button(
            onClick = onAddClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp)
        ) {
            Text("Добавить лекарство")
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Подтвердите удаление") },
            text = { Text("Вы уверены, что хотите удалить это лекарство?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        medicineToDelete?.let {
                            onDeleteClick(it)
                        }
                        showDeleteDialog = false
                    }
                ) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Нет")
                }
            }
        )
    }
}



    @Composable
fun MedicineCard(
    medicine: Medicine,
    time: Date,
    selectedDate: Date,
    onEditClick: (Medicine) -> Unit,
    onDeleteClick: (Medicine) -> Unit,
    onStatusChange: (Medicine, Date, Date) -> Unit,
    isTaken: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Название: ${medicine.name}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Описание: ${medicine.description}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Дозировка: ${medicine.dosage}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Повторяемость: ${medicine.repetition}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Дата окончания: ${SimpleDateFormat("dd/MM/yyyy").format(medicine.endDate)}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Дата начала: ${SimpleDateFormat("dd/MM/yyyy").format(medicine.startDate)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(text = "Время: ${SimpleDateFormat("HH:mm").format(time)}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = if (isTaken) "Статус: Принято" else "Статус: Не принято",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isTaken) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = { onEditClick(medicine) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Редактировать")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onDeleteClick(medicine) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Удалить")
                    }

                }
            }
        }

        IconButton(
            onClick = {
                val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(time)
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate)
                onStatusChange(medicine, selectedDate, time)
            },
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(
                    id = if (isTaken) R.drawable.ic_check_circle else R.drawable.ic_circle
                ),
                contentDescription = if (isTaken) "Принято" else "Не принято",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

fun isMedicineDueOnDate(medicine: Medicine, selectedDate: Date): Boolean {
    val startCalendar = Calendar.getInstance().apply { time = medicine.startDate }
    val endCalendar = Calendar.getInstance().apply { time = medicine.endDate }
    val selectedCalendar = Calendar.getInstance().apply { time = selectedDate }

    if (selectedDate.before(medicine.startDate)) {
        return false
    }

    return when (medicine.repetition) {
        "Ежедневно" -> {
            selectedDate.before(medicine.endDate) || selectedDate == medicine.endDate
        }
        "Еженедельно" -> {
            val daysDifference = (selectedDate.time - medicine.startDate.time) / (1000 * 60 * 60 * 24)
            daysDifference % 7 == 0L && selectedDate.before(medicine.endDate)
        }
        "Через день" -> {
            val daysDifference = (selectedDate.time - medicine.startDate.time) / (1000 * 60 * 60 * 24)
            daysDifference % 2 == 0L && selectedDate.before(medicine.endDate)
        }
        else -> false
    }
}

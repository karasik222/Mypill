package com.example.mypill
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mypill.database.Medicine
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddMedicineScreen(
    onSave: (Medicine) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var repeat by remember { mutableStateOf("Ежедневно") }
    var startDate by remember { mutableStateOf(Date()) }
    var endDate by remember { mutableStateOf(Date()) }
    var consumptionTimes by remember { mutableStateOf(listOf<Date>()) }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val options = listOf("Ежедневно", "Через день", "Еженедельно")

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Название лекарства") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Описание") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = dosage,
            onValueChange = {
                if (it.matches(Regex("^\\d*\\.?\\d*\$"))) {
                    dosage = it
                }
            },
            label = { Text("Дозировка (мг)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal
            )
        )

        Column {
            options.forEach { option ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    RadioButton(
                        selected = repeat == option,
                        onClick = { repeat = option }
                    )
                    Text(text = option, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        Button(onClick = {
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    startDate = GregorianCalendar(year, month, dayOfMonth).time
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            ).show()
        }) {
            Text("Выбрать дату начала")
        }

        Text(
            text = "Дата начала: ${SimpleDateFormat("dd/MM/yyyy").format(startDate)}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        Button(onClick = {
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    endDate = GregorianCalendar(year, month, dayOfMonth).time
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            ).show()
        }) {
            Text("Выбрать дату окончания")
        }

        Text(
            text = "Дата окончания: ${SimpleDateFormat("dd/MM/yyyy").format(endDate)}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        Button(onClick = {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    val time = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, hourOfDay)
                        set(Calendar.MINUTE, minute)
                    }.time
                    consumptionTimes = consumptionTimes + time
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }) {
            Text("Добавить время")
        }

        LazyColumn {
            items(consumptionTimes) { time ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Время: ${SimpleDateFormat("HH:mm").format(time)}", style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = { consumptionTimes = consumptionTimes - time }) {
                        Text("Удалить")
                    }
                }
            }
        }

        Button(
            onClick = {
                if (name.isNotBlank() && description.isNotBlank() && dosage.isNotBlank()) {
                    val medicine = Medicine(
                        name = name,
                        description = description,
                        isTaken = false,
                        dosage = dosage.toDouble(),
                        repetition = repeat,
                        startDate = startDate,
                        endDate = endDate,
                        consumptionTimes = consumptionTimes
                    )
                    onSave(medicine)
                } else {
                    Toast.makeText(context, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }
    }
}

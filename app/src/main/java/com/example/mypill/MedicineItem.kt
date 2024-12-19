package com.example.mypill

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import com.example.mypill.database.Medicine
import java.util.*

@Composable
fun MedicineCard(medicine: Medicine, time: Date, key: String) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${medicine.name}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Description: ${medicine.description}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Dosage: ${medicine.dosage}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Repeat: ${medicine.repetition}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "End Date: ${SimpleDateFormat("dd/MM/yyyy").format(medicine.endDate)}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Time: ${SimpleDateFormat("HH:mm").format(time)}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

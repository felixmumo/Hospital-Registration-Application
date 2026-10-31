package com.example.hospital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hospital.data.model.Appointment
import com.example.hospital.data.model.Doctor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    appointments: List<Appointment>,
    doctors: List<Doctor>,
    onAddAppointment: (String, String, String, String, String) -> Unit,
    onUpdateStatus: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Appointment Scheduling") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "New Appointment")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Upcoming Consultations & Surgeries",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(appointments) { appt ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = appt.patientName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Badge(
                                containerColor = when (appt.status) {
                                    "Completed" -> MaterialTheme.colorScheme.tertiary
                                    "Cancelled" -> MaterialTheme.colorScheme.error
                                    else -> MaterialTheme.colorScheme.primary
                                }
                            ) {
                                Text(appt.status, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }

                        Text("Doctor: ${appt.doctorName} (${appt.department})", style = MaterialTheme.typography.bodyMedium)
                        Text("Date: ${appt.date} at ${appt.time}", style = MaterialTheme.typography.bodySmall)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (appt.status == "Scheduled") {
                                TextButton(onClick = { onUpdateStatus(appt.id, "Completed") }) {
                                    Text("Mark Complete")
                                }
                                TextButton(onClick = { onUpdateStatus(appt.id, "Cancelled") }) {
                                    Text("Cancel", color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddAppointmentDialog(
            doctors = doctors,
            onDismiss = { showDialog = false },
            onConfirm = { patient, doctor, date, time, dept ->
                onAddAppointment(patient, doctor, date, time, dept)
                showDialog = false
            }
        )
    }
}

@Composable
fun AddAppointmentDialog(
    doctors: List<Doctor>,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String) -> Unit
) {
    var patientName by remember { mutableStateOf("") }
    var selectedDoctor by remember { mutableStateOf(doctors.firstOrNull()?.name ?: "Dr. Sarah Jenkins") }
    var department by remember { mutableStateOf("Cardiology") }
    var date by remember { mutableStateOf("2026-10-31") }
    var time by remember { mutableStateOf("09:00 AM") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Schedule Appointment") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = patientName, onValueChange = { patientName = it }, label = { Text("Patient Name") }, singleLine = true)
                OutlinedTextField(value = selectedDoctor, onValueChange = { selectedDoctor = it }, label = { Text("Assigned Doctor") }, singleLine = true)
                OutlinedTextField(value = department, onValueChange = { department = it }, label = { Text("Department") }, singleLine = true)
                OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date (YYYY-MM-DD)") }, singleLine = true)
                OutlinedTextField(value = time, onValueChange = { time = it }, label = { Text("Time (e.g., 10:00 AM)") }, singleLine = true)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (patientName.isNotBlank()) {
                        onConfirm(patientName, selectedDoctor, date, time, department)
                    }
                }
            ) {
                Text("Schedule")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

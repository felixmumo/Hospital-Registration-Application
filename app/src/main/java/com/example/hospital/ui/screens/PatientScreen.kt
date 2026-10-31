package com.example.hospital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hospital.data.model.Patient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientScreen(
    patients: List<Patient>,
    onAddPatient: (String, Int, String, String, String, String) -> Unit,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredPatients = patients.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.diagnosis.contains(searchQuery, ignoreCase = true) ||
                it.roomNumber.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Patient Management") },
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
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Patient")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search by name, diagnosis, or room...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            // Patient List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredPatients) { patient ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = patient.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Badge(
                                    containerColor = if (patient.status == "Critical")
                                        MaterialTheme.colorScheme.error
                                    else
                                        MaterialTheme.colorScheme.primary
                                ) {
                                    Text(patient.status, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Text(text = "Diagnosis: ${patient.diagnosis}", style = MaterialTheme.typography.bodyMedium)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Room: ${patient.roomNumber}", style = MaterialTheme.typography.bodySmall)
                                Text(text = "Age: ${patient.age} | Gender: ${patient.gender}", style = MaterialTheme.typography.bodySmall)
                                Text(text = "Admitted: ${patient.admissionDate}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddPatientDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, age, gender, diag, room, date ->
                onAddPatient(name, age, gender, diag, room, date)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AddPatientDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ageStr by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var diagnosis by remember { mutableStateOf("") }
    var roomNumber by remember { mutableStateOf("") }
    var admissionDate by remember { mutableStateOf("2026-10-30") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Admit New Patient") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Patient Full Name") }, singleLine = true)
                OutlinedTextField(value = ageStr, onValueChange = { ageStr = it }, label = { Text("Age") }, singleLine = true)
                OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender (Male/Female/Other)") }, singleLine = true)
                OutlinedTextField(value = diagnosis, onValueChange = { diagnosis = it }, label = { Text("Diagnosis") }, singleLine = true)
                OutlinedTextField(value = roomNumber, onValueChange = { roomNumber = it }, label = { Text("Room Number (e.g., Room 302)") }, singleLine = true)
                OutlinedTextField(value = admissionDate, onValueChange = { admissionDate = it }, label = { Text("Admission Date (YYYY-MM-DD)") }, singleLine = true)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val age = ageStr.toIntOrNull() ?: 30
                    if (name.isNotBlank()) {
                        onConfirm(name, age, gender, diagnosis, roomNumber, admissionDate)
                    }
                }
            ) {
                Text("Admit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

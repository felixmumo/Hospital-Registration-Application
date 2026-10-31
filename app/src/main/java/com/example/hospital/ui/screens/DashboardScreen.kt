package com.example.hospital.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hospital.data.model.Appointment
import com.example.hospital.data.model.HospitalStats
import com.example.hospital.data.model.Patient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    stats: HospitalStats,
    patients: List<Patient>,
    appointments: List<Appointment>,
    isFirebaseConnected: Boolean,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MediCore Hospital Dashboard", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { onNavigate("analytics") }) {
                        Icon(Icons.Default.Analytics, contentDescription = "Analytics")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Firebase Status Banner
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isFirebaseConnected)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else
                            MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = if (isFirebaseConnected) Icons.Default.CloudDone else Icons.Default.CloudOff,
                            contentDescription = "Sync Status"
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isFirebaseConnected) "Smart Firebase Cloud Active" else "Offline Cache Mode Active",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isFirebaseConnected) "Real-time Firestore synchronization enabled" else "Connected to local fallback repository. Add google-services.json for cloud sync.",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            // Statistics Grid Header
            item {
                Text(
                    text = "Hospital Overview",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // Stats Cards
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Total Patients",
                            value = stats.totalPatients.toString(),
                            icon = Icons.Default.People,
                            onClick = { onNavigate("patients") }
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Available Rooms",
                            value = stats.availableRooms.toString(),
                            icon = Icons.Default.MeetingRoom,
                            onClick = { onNavigate("inventory") }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Active Doctors",
                            value = stats.activeDoctors.toString(),
                            icon = Icons.Default.Person,
                            onClick = { onNavigate("doctors") }
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Emergency Cases",
                            value = stats.emergencyCases.toString(),
                            icon = Icons.Default.Warning,
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            onClick = { onNavigate("patients") }
                        )
                    }
                }
            }

            // Quick Jump Actions
            item {
                Text(
                    text = "Quick Navigation",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { onNavigate("patients") }) {
                        Icon(Icons.Default.Group, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Patients")
                    }
                    Button(onClick = { onNavigate("appointments") }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Schedule")
                    }
                    Button(onClick = { onNavigate("inventory") }) {
                        Icon(Icons.Default.Inventory2, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Inventory")
                    }
                }
            }

            // Recent Critical Patients
            item {
                Text(
                    text = "Admitted Patients Overview",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(patients.take(3)) { patient ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(patient.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Badge(
                                containerColor = if (patient.status == "Critical") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            ) {
                                Text(patient.status, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Text("Diagnosis: ${patient.diagnosis}", style = MaterialTheme.typography.bodyMedium)
                        Text("Room: ${patient.roomNumber} | Age: ${patient.age} (${patient.gender})", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surfaceVariant,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
            Text(text = value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

package com.example.hospital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hospital.data.model.Doctor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorsScreen(
    doctors: List<Doctor>,
    onToggleAvailability: (String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Medical Staff & Doctors") },
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
                    text = "On-Duty Specialists & Consultants",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(doctors) { doctor ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                            Column {
                                Text(text = doctor.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(text = doctor.specialty, style = MaterialTheme.typography.bodyMedium)
                                Text(text = "Room: ${doctor.room} | Phone: ${doctor.phone}", style = MaterialTheme.typography.bodySmall)
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Badge(
                                containerColor = if (doctor.isAvailable) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
                            ) {
                                Text(
                                    text = if (doctor.isAvailable) "Available" else "Busy",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            TextButton(onClick = { onToggleAvailability(doctor.id) }) {
                                Text(if (doctor.isAvailable) "Set Busy" else "Set Available")
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.example.hospital.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hospital.data.model.InventoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    inventory: List<InventoryItem>,
    onAddInventory: (String, String, Int, String, String) -> Unit,
    onBack: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pharmacy & Medical Inventory") },
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
                Icon(Icons.Default.Add, contentDescription = "Add Item")
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
                    text = "Medical Stock & Supplies",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(inventory) { item ->
                val isLowStock = item.stockQuantity < 150
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isLowStock)
                            MaterialTheme.colorScheme.errorContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                if (isLowStock) {
                                    Icon(Icons.Default.Warning, contentDescription = "Low Stock", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                                }
                            }
                            Text(text = "Category: ${item.category}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Expiry: ${item.expiryDate}", style = MaterialTheme.typography.bodySmall)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${item.stockQuantity} ${item.unit}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            if (isLowStock) {
                                Text(
                                    text = "Low Stock!",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddInventoryDialog(
            onDismiss = { showDialog = false },
            onConfirm = { name, category, qty, unit, expiry ->
                onAddInventory(name, category, qty, unit, expiry)
                showDialog = false
            }
        )
    }
}

@Composable
fun AddInventoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Pharmacy") }
    var qtyStr by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("Tablets") }
    var expiry by remember { mutableStateOf("2027-12-31") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Inventory Item") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Item Name") }, singleLine = true)
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category (Pharmacy/PPE/Fluids)") }, singleLine = true)
                OutlinedTextField(value = qtyStr, onValueChange = { qtyStr = it }, label = { Text("Stock Quantity") }, singleLine = true)
                OutlinedTextField(value = unit, onValueChange = { unit = it }, label = { Text("Unit (Tablets/Pieces/Vials)") }, singleLine = true)
                OutlinedTextField(value = expiry, onValueChange = { expiry = it }, label = { Text("Expiry Date (YYYY-MM-DD)") }, singleLine = true)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val qty = qtyStr.toIntOrNull() ?: 100
                    if (name.isNotBlank()) {
                        onConfirm(name, category, qty, unit, expiry)
                    }
                }
            ) {
                Text("Add Item")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

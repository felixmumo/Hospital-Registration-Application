package com.example.hospital.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Home", Icons.Default.Home)
    object Patients : Screen("patients", "Patients", Icons.Default.People)
    object Appointments : Screen("appointments", "Schedule", Icons.Default.CalendarMonth)
    object Doctors : Screen("doctors", "Doctors", Icons.Default.Person)
    object Inventory : Screen("inventory", "Inventory", Icons.Default.Inventory2)
    object Analytics : Screen("analytics", "Analytics", Icons.Default.Analytics)
}

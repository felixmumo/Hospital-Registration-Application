package com.example.hospital.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hospital.ui.screens.*
import com.example.hospital.ui.viewmodel.HospitalViewModel

@Composable
fun HospitalNavGraph(viewModel: HospitalViewModel) {
    val navController = rememberNavController()

    val stats by viewModel.stats.collectAsState()
    val patients by viewModel.patients.collectAsState()
    val appointments by viewModel.appointments.collectAsState()
    val doctors by viewModel.doctors.collectAsState()
    val inventory by viewModel.inventory.collectAsState()
    val isFirebaseConnected by viewModel.isFirebaseConnected.collectAsState()

    val bottomNavItems = listOf(
        Screen.Dashboard,
        Screen.Patients,
        Screen.Appointments,
        Screen.Doctors,
        Screen.Inventory
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Show bottom bar only on main navigation destinations
    val showBottomBar = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    stats = stats,
                    patients = patients,
                    appointments = appointments,
                    isFirebaseConnected = isFirebaseConnected,
                    onNavigate = { route -> navController.navigate(route) }
                )
            }
            composable(Screen.Patients.route) {
                PatientScreen(
                    patients = patients,
                    onAddPatient = { name, age, gender, diag, room, date ->
                        viewModel.addPatient(name, age, gender, diag, room, date)
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Appointments.route) {
                AppointmentsScreen(
                    appointments = appointments,
                    doctors = doctors,
                    onAddAppointment = { patient, doctor, date, time, dept ->
                        viewModel.addAppointment(patient, doctor, date, time, dept)
                    },
                    onUpdateStatus = { id, status ->
                        viewModel.updateAppointmentStatus(id, status)
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Doctors.route) {
                DoctorsScreen(
                    doctors = doctors,
                    onToggleAvailability = { doctorId ->
                        viewModel.toggleDoctorAvailability(doctorId)
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Inventory.route) {
                InventoryScreen(
                    inventory = inventory,
                    onAddInventory = { name, category, qty, unit, expiry ->
                        viewModel.addInventoryItem(name, category, qty, unit, expiry)
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Analytics.route) {
                AnalyticsScreen(
                    stats = stats,
                    isFirebaseConnected = isFirebaseConnected,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

package com.example.hospital.data.model

data class Patient(
    val id: String = "",
    val name: String = "",
    val age: Int = 0,
    val gender: String = "Other",
    val diagnosis: String = "",
    val roomNumber: String = "",
    val admissionDate: String = "",
    val status: String = "Admitted" // Admitted, Discharged, Critical
)

data class Appointment(
    val id: String = "",
    val patientName: String = "",
    val doctorName: String = "",
    val date: String = "",
    val time: String = "",
    val department: String = "",
    val status: String = "Scheduled" // Scheduled, Completed, Cancelled
)

data class Doctor(
    val id: String = "",
    val name: String = "",
    val specialty: String = "",
    val room: String = "",
    val phone: String = "",
    val isAvailable: Boolean = true
)

data class InventoryItem(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val stockQuantity: Int = 0,
    val unit: String = "Units",
    val expiryDate: String = ""
)

data class HospitalStats(
    val totalPatients: Int = 124,
    val availableRooms: Int = 18,
    val activeDoctors: Int = 32,
    val emergencyCases: Int = 5,
    val bedOccupancyRate: Float = 0.85f
)

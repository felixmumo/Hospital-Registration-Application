package com.example.hospital.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospital.data.model.Appointment
import com.example.hospital.data.model.Doctor
import com.example.hospital.data.model.HospitalStats
import com.example.hospital.data.model.InventoryItem
import com.example.hospital.data.model.Patient
import com.example.hospital.data.repository.HospitalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HospitalViewModel : ViewModel() {
    private val repository = HospitalRepository()

    val isFirebaseConnected: StateFlow<Boolean> = repository.isFirebaseConnected
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val patients: StateFlow<List<Patient>> = repository.getPatients()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val appointments: StateFlow<List<Appointment>> = repository.getAppointments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val doctors: StateFlow<List<Doctor>> = repository.getDoctors()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val inventory: StateFlow<List<InventoryItem>> = repository.getInventory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stats: StateFlow<HospitalStats> = repository.getStats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HospitalStats())

    fun addPatient(name: String, age: Int, gender: String, diagnosis: String, roomNumber: String, admissionDate: String) {
        viewModelScope.launch {
            val patient = Patient(
                name = name,
                age = age,
                gender = gender,
                diagnosis = diagnosis,
                roomNumber = roomNumber,
                admissionDate = admissionDate,
                status = "Admitted"
            )
            repository.addPatient(patient)
        }
    }

    fun addAppointment(patientName: String, doctorName: String, date: String, time: String, department: String) {
        viewModelScope.launch {
            val appt = Appointment(
                patientName = patientName,
                doctorName = doctorName,
                date = date,
                time = time,
                department = department,
                status = "Scheduled"
            )
            repository.addAppointment(appt)
        }
    }

    fun updateAppointmentStatus(appointmentId: String, status: String) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(appointmentId, status)
        }
    }

    fun toggleDoctorAvailability(doctorId: String) {
        viewModelScope.launch {
            repository.toggleDoctorAvailability(doctorId)
        }
    }

    fun addInventoryItem(name: String, category: String, quantity: Int, unit: String, expiryDate: String) {
        viewModelScope.launch {
            val item = InventoryItem(
                name = name,
                category = category,
                stockQuantity = quantity,
                unit = unit,
                expiryDate = expiryDate
            )
            repository.addInventoryItem(item)
        }
    }
}

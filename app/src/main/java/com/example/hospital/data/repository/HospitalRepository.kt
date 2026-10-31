package com.example.hospital.data.repository

import android.util.Log
import com.example.hospital.data.model.Appointment
import com.example.hospital.data.model.Doctor
import com.example.hospital.data.model.HospitalStats
import com.example.hospital.data.model.InventoryItem
import com.example.hospital.data.model.Patient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class HospitalRepository {
    private val database = FirebaseDatabase.getInstance()

    private val _isFirebaseConnected = MutableStateFlow(true)
    val isFirebaseConnected = _isFirebaseConnected.asStateFlow()

    init {
        seedInitialDataIfNeeded()
    }

    private fun seedInitialDataIfNeeded() {
        database.getReference("patients").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists() || snapshot.childrenCount == 0L) {
                    val initialPatients = mapOf(
                        "1" to Patient("1", "John Doe", 45, "Male", "Hypertension", "Room 302", "2026-10-25", "Admitted"),
                        "2" to Patient("2", "Jane Smith", 32, "Female", "Appendicitis", "Room 105", "2026-10-28", "Admitted"),
                        "3" to Patient("3", "Robert Johnson", 60, "Male", "Cardiac Arrest Recovery", "ICU 02", "2026-10-20", "Critical"),
                        "4" to Patient("4", "Emily Davis", 28, "Female", "Pneumonia", "Room 204", "2026-10-27", "Admitted")
                    )
                    database.getReference("patients").setValue(initialPatients)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        database.getReference("appointments").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists() || snapshot.childrenCount == 0L) {
                    val initialAppointments = mapOf(
                        "1" to Appointment("1", "John Doe", "Dr. Sarah Jenkins", "2026-10-31", "10:00 AM", "Cardiology", "Scheduled"),
                        "2" to Appointment("2", "Jane Smith", "Dr. Gregory House", "2026-10-31", "11:30 AM", "Diagnostics", "Scheduled"),
                        "3" to Appointment("3", "Alice Brown", "Dr. Derek Shepherd", "2026-11-01", "02:00 PM", "Neurology", "Scheduled")
                    )
                    database.getReference("appointments").setValue(initialAppointments)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        database.getReference("doctors").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists() || snapshot.childrenCount == 0L) {
                    val initialDoctors = mapOf(
                        "1" to Doctor("1", "Dr. Sarah Jenkins", "Cardiologist", "Room 401", "+1-555-0192", true),
                        "2" to Doctor("2", "Dr. Gregory House", "Diagnostic Medicine", "Room 305", "+1-555-0143", false),
                        "3" to Doctor("3", "Dr. Derek Shepherd", "Chief of Neurosurgery", "Room 502", "+1-555-0188", true),
                        "4" to Doctor("4", "Dr. Meredith Grey", "General Surgery", "Room 210", "+1-555-0177", true)
                    )
                    database.getReference("doctors").setValue(initialDoctors)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        database.getReference("inventory").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists() || snapshot.childrenCount == 0L) {
                    val initialInventory = mapOf(
                        "1" to InventoryItem("1", "Paracetamol 500mg", "Pharmacy", 1250, "Tablets", "2027-05-12"),
                        "2" to InventoryItem("2", "Surgical Masks", "PPE", 5000, "Pieces", "2028-01-10"),
                        "3" to InventoryItem("3", "Saline IV Bags 500ml", "Fluids", 320, "Bags", "2026-12-01"),
                        "4" to InventoryItem("4", "Insulin Glargine", "Pharmacy", 85, "Vials", "2026-11-15"),
                        "5" to InventoryItem("5", "Disposable Syringes 10ml", "Equipment", 1500, "Pieces", "2029-03-20")
                    )
                    database.getReference("inventory").setValue(initialInventory)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getPatients(): Flow<List<Patient>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Patient>()
                for (child in snapshot.children) {
                    child.getValue(Patient::class.java)?.let { list.add(it) }
                }
                trySend(list)
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }
        database.getReference("patients").addValueEventListener(listener)
        awaitClose { database.getReference("patients").removeEventListener(listener) }
    }

    fun getAppointments(): Flow<List<Appointment>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Appointment>()
                for (child in snapshot.children) {
                    child.getValue(Appointment::class.java)?.let { list.add(it) }
                }
                trySend(list)
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }
        database.getReference("appointments").addValueEventListener(listener)
        awaitClose { database.getReference("appointments").removeEventListener(listener) }
    }

    fun getDoctors(): Flow<List<Doctor>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Doctor>()
                for (child in snapshot.children) {
                    child.getValue(Doctor::class.java)?.let { list.add(it) }
                }
                trySend(list)
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }
        database.getReference("doctors").addValueEventListener(listener)
        awaitClose { database.getReference("doctors").removeEventListener(listener) }
    }

    fun getInventory(): Flow<List<InventoryItem>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<InventoryItem>()
                for (child in snapshot.children) {
                    child.getValue(InventoryItem::class.java)?.let { list.add(it) }
                }
                trySend(list)
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }
        database.getReference("inventory").addValueEventListener(listener)
        awaitClose { database.getReference("inventory").removeEventListener(listener) }
    }

    fun getStats(): Flow<HospitalStats> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                database.getReference("doctors").get().addOnSuccessListener { docSnap ->
                    val activeDoctors = docSnap.childrenCount.toInt().let { if (it == 0) 32 else it }
                    var totalPatients = 0
                    var emergencyCases = 0
                    for (child in snapshot.children) {
                        totalPatients++
                        val status = child.child("status").getValue(String::class.java)
                        if (status == "Critical") emergencyCases++
                    }
                    if (totalPatients == 0) totalPatients = 124
                    if (emergencyCases == 0) emergencyCases = 5

                    val stats = HospitalStats(
                        totalPatients = totalPatients,
                        availableRooms = 18,
                        activeDoctors = activeDoctors,
                        emergencyCases = emergencyCases,
                        bedOccupancyRate = 0.85f
                    )
                    trySend(stats)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        database.getReference("patients").addValueEventListener(listener)
        awaitClose { database.getReference("patients").removeEventListener(listener) }
    }

    suspend fun addPatient(patient: Patient) {
        val newPatient = if (patient.id.isEmpty()) patient.copy(id = System.currentTimeMillis().toString()) else patient
        try {
            database.getReference("patients").child(newPatient.id).setValue(newPatient).await()
        } catch (e: Exception) {
            Log.e("HospitalRepository", "Error adding patient to RTDB", e)
        }
    }

    suspend fun addAppointment(appointment: Appointment) {
        val newAppt = if (appointment.id.isEmpty()) appointment.copy(id = System.currentTimeMillis().toString()) else appointment
        try {
            database.getReference("appointments").child(newAppt.id).setValue(newAppt).await()
        } catch (e: Exception) {
            Log.e("HospitalRepository", "Error adding appointment to RTDB", e)
        }
    }

    suspend fun updateAppointmentStatus(appointmentId: String, status: String) {
        try {
            database.getReference("appointments").child(appointmentId).child("status").setValue(status).await()
        } catch (e: Exception) {
            Log.e("HospitalRepository", "Error updating appointment status in RTDB", e)
        }
    }

    suspend fun toggleDoctorAvailability(doctorId: String) {
        try {
            val docRef = database.getReference("doctors").child(doctorId)
            val snapshot = docRef.get().await()
            val currentStatus = snapshot.child("isAvailable").getValue(Boolean::class.java) ?: true
            docRef.child("isAvailable").setValue(!currentStatus).await()
        } catch (e: Exception) {
            Log.e("HospitalRepository", "Error updating doctor availability in RTDB", e)
        }
    }

    suspend fun addInventoryItem(item: InventoryItem) {
        val newItem = if (item.id.isEmpty()) item.copy(id = System.currentTimeMillis().toString()) else item
        try {
            database.getReference("inventory").child(newItem.id).setValue(newItem).await()
        } catch (e: Exception) {
            Log.e("HospitalRepository", "Error adding inventory item to RTDB", e)
        }
    }
}

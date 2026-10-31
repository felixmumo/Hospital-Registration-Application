package com.example.hospital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hospital.ui.navigation.HospitalNavGraph
import com.example.hospital.ui.theme.HospitalTheme
import com.example.hospital.ui.viewmodel.HospitalViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HospitalTheme {
                val viewModel: HospitalViewModel = viewModel()
                HospitalNavGraph(viewModel = viewModel)
            }
        }
    }
}

package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.screens.*
import com.example.ui.theme.VetSathiTheme
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VetSathiViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: VetSathiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VetSathiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val currentScreen by viewModel.currentScreen.collectAsState()

                    Crossfade(
                        targetState = currentScreen,
                        animationSpec = tween(durationMillis = 350),
                        label = "ScreenTransition"
                    ) { screen ->
                        when (screen) {
                            Screen.ROLE_SELECTION -> RoleSelectionScreen(viewModel)
                            Screen.AUTH -> AuthScreen(viewModel)
                            Screen.OWNER_HOME -> OwnerDashboardScreen(viewModel)
                            Screen.BOOKING_FLOW -> BookingFlowScreen(viewModel)
                            Screen.DOCTOR_DASHBOARD -> DoctorDashboardScreen(viewModel)
                            Screen.ADMIN_DASHBOARD -> AdminDashboardScreen(viewModel)
                            Screen.PHARMACY_DASHBOARD -> PharmacyDashboardScreen(viewModel)
                            Screen.LAB_DASHBOARD -> LabDashboardScreen(viewModel)
                            Screen.AI_ASSISTANT -> AiAssistantScreen(viewModel)
                        }
                    }
                }
            }
        }
    }
}

package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.VetSathiPrimary
import com.example.ui.theme.VetSathiSecondary
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VetSathiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabDashboardScreen(viewModel: VetSathiViewModel) {
    var samplesCount by remember { mutableStateOf(4) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pashu Sewa Sathi Diagnostics Lab Console", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                actions = {
                    IconButton(onClick = { viewModel.logOut() }) {
                        Icon(Icons.Filled.Logout, "Log Out", tint = Color.Red)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAF9))
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats Row
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("ACTIVE PATHOLOGY REQUESTS", fontSize = 8.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text("$samplesCount Tests", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = VetSathiSecondary)
                        Text("ETA 24 Hours reporting", fontSize = 9.sp, color = Color.Gray)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("REVENUE EARNED", fontSize = 8.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text("₹8,450", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = VetSathiPrimary)
                        Text("Instant bank payout synced", fontSize = 9.sp, color = Color.Gray)
                    }
                }
            }

            Text("PENDING PATHOLOGY LABORATORY ASSIGNMENTS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)

            if (samplesCount == 0) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text("All biological tests analyzed and uploaded!", color = Color.Gray)
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("TEST ID: VS-LAB-8812", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFE0F2FE), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("SAMPLE RECEIVED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0369A1))
                            }
                        }

                        Divider()

                        Text("Sample Type: Bovine Blood Sample (Gir Cow)\nPatient Name: Ganga\nDoctor Referred: Dr. Rajesh Sharma\nTest Requested: Brucellosis PCR & Milk Quality analysis", fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(4.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(
                                onClick = { samplesCount-- },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Reject Sample")
                            }

                            Button(
                                onClick = { samplesCount-- },
                                colors = ButtonDefaults.buttonColors(containerColor = VetSathiPrimary),
                                modifier = Modifier.weight(1.5f)
                            ) {
                                Text("Upload Report PDF", color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

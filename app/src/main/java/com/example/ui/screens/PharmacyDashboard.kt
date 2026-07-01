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
fun PharmacyDashboardScreen(viewModel: VetSathiViewModel) {
    var stockCount by remember { mutableStateOf(1450) }
    var pendingOrders by remember { mutableStateOf(2) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("VetSathi Partner Pharmacy Portal", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
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
                        Text("MEDICINE INVENTORY", fontSize = 8.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text("$stockCount Items", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("48 Lifesaving drugs cataloged", fontSize = 9.sp, color = Color.Gray)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("PENDING DELIVERIES", fontSize = 8.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text("$pendingOrders Orders", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = VetSathiSecondary)
                        Text("ETA 15m Delivery matching", fontSize = 9.sp, color = Color.Gray)
                    }
                }
            }

            Text("MEDICINE ORDERS PACKAGING QUEUE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)

            if (pendingOrders == 0) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text("All medical orders dispatched!", color = Color.Gray)
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
                            Text("ORDER VS-CHEM-9821", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFEF3C7), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("READY TO SHIP", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                            }
                        }

                        Divider()

                        Text("Items:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                        Text("• Injection Melonex (Meloxicam 5mg/ml) - 2 vials\n• Capsule Rumen FS (Stomach Bloat cure) - 4 packs\n• Sterile surgical syringe & gauze - 1 packet", fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(4.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(
                                onClick = { pendingOrders-- },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Out of Stock")
                            }

                            Button(
                                onClick = { pendingOrders-- },
                                colors = ButtonDefaults.buttonColors(containerColor = VetSathiPrimary),
                                modifier = Modifier.weight(1.5f)
                            ) {
                                Text("Dispatch Order", color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

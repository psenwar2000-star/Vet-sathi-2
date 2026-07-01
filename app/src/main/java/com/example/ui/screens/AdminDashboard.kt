package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun AdminDashboardScreen(viewModel: VetSathiViewModel) {
    var totalRegistrationsCount by remember { mutableStateOf(4821) }
    var verifiedDocsCount by remember { mutableStateOf(142) }
    var pendingApprovalsCount by remember { mutableStateOf(5) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pashu Sewa Sathi Super-Admin Console", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
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
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                AdminStatCard("USERS", totalRegistrationsCount.toString(), "👁 14 New Today", modifier = Modifier.weight(1f))
                AdminStatCard("VERIFIED VETS", verifiedDocsCount.toString(), "✔ 100% Licensed", modifier = Modifier.weight(1f))
                AdminStatCard("PENDING VETS", pendingApprovalsCount.toString(), "⏰ Review credentials", modifier = Modifier.weight(1f))
            }

            // Commission & Financial Metrics Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("PLATFORM FINANCIAL AUDITS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total GTV Practice Revenue", fontSize = 11.sp, color = Color.Gray)
                            Text("₹14,50,000", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }

                        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFD1FAE5))) {
                            Text(
                                "Commission: 15%",
                                color = Color(0xFF065F46),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Doctor License Approval Section
            Text("VET REGISTRATION APPROVALS QUEUE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)

            if (pendingApprovalsCount == 0) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text("No pending veterinary registrations.", color = Color.Gray)
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(VetSathiSecondary.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🎓", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Dr. Shreya Joshi", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Degree: B.V.Sc & A.H | Univ of Veterinary Sciences, Bikaner", fontSize = 10.sp, color = Color.Gray)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF1F5F9), RoundedCornerShape(10.dp))
                                .padding(8.dp)
                        ) {
                            Text("Govt License Uploaded: VCI-REG-2026-8812 (File: license_shreya.pdf)", fontSize = 10.sp, color = Color.DarkGray)
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(
                                onClick = { pendingApprovalsCount-- },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Reject License")
                            }

                            Button(
                                onClick = {
                                    pendingApprovalsCount--
                                    verifiedDocsCount++
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = VetSathiPrimary),
                                modifier = Modifier.weight(1.5f)
                            ) {
                                Text("Approve & Onboard", color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    footer: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, fontSize = 8.5.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(footer, fontSize = 8.sp, color = Color.DarkGray)
        }
    }
}

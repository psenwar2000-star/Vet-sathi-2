package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingEntity
import com.example.ui.theme.VetSathiPrimary
import com.example.ui.theme.VetSathiSecondary
import com.example.ui.theme.VetSathiTertiary
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VetSathiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorDashboardScreen(viewModel: VetSathiViewModel) {
    val isDoctorOnline by viewModel.isDoctorOnline.collectAsState()
    val bookingsList by viewModel.bookings.collectAsState()
    val walletBalance by viewModel.walletBalance.collectAsState()

    var activeDiagnosisText by remember { mutableStateOf("") }
    var showPrescriptionCreatorForBooking by remember { mutableStateOf<BookingEntity?>(null) }
    var prescriptionDraftText by remember { mutableStateOf("") }
    var isAIPrescriptionDraftLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dr. Anand Verma - Clinic Dashboard", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
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
            // Profile Verification Badge & Status Toggle
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(VetSathiPrimary.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🩺", fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Dr. Anand Verma", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Filled.Verified, "Verified", tint = VetSathiPrimary, modifier = Modifier.size(16.dp))
                            }
                            Text("Reg No: RAJ-VET-2026-9912", fontSize = 10.sp, color = Color.Gray)
                        }
                    }

                    // Online/Offline Switch
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isDoctorOnline) "ONLINE" else "OFFLINE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDoctorOnline) VetSathiPrimary else Color.Gray,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Switch(
                            checked = isDoctorOnline,
                            onCheckedChange = { viewModel.setDoctorOnline(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = VetSathiPrimary
                            ),
                            modifier = Modifier.testTag("online_offline_switch")
                        )
                    }
                }
            }

            // Doctor Live Radar Statistics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("DAILY EARNINGS", fontSize = 8.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text("₹4,250", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("8 Active Visits completed", fontSize = 9.sp, color = Color.Gray)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("VETSATHI WALLET", fontSize = 8.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text("₹12,450", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = VetSathiSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Click to instant withdraw", fontSize = 9.sp, color = VetSathiPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // High Fidelity Earnings Chart Block
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "WEEKLY PRACTICE REVENUE ANALYTICS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Graph Bars
                        BarGraphColumn("Mon", 1500f, 3000f)
                        BarGraphColumn("Tue", 2200f, 3000f)
                        BarGraphColumn("Wed", 1100f, 3000f)
                        BarGraphColumn("Thu", 3100f, 3000f)
                        BarGraphColumn("Fri", 2500f, 3000f)
                        BarGraphColumn("Sat", 4200f, 3000f)
                        BarGraphColumn("Sun", 800f, 3000f)
                    }
                }
            }

            // Cases dispatch section
            Text(
                text = "DISPATCH RADAR - INCOMING CASES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                letterSpacing = 1.sp
            )

            if (bookingsList.none { it.status == "Pending" || it.status == "Accepted" || it.status == "Active" }) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("No pending local veterinary requests in your 15km radius currently.", color = Color.Gray, fontSize = 12.sp, textAlign = TextAlign.Center)
                    }
                }
            }

            bookingsList.filter { it.status == "Pending" || it.status == "Accepted" || it.status == "Active" }.forEach { booking ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row {
                                Text(if (booking.animalName.contains("Cow", ignoreCase = true)) "🐄" else "🐕", fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(booking.serviceType, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text("Animal: ${booking.animalName}", fontSize = 11.sp, color = Color.Gray)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFEF3C7))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = booking.status.uppercase(),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD97706)
                                )
                            }
                        }

                        Text(
                            text = "Client Location: Jodhpur, Rajasthan (Distance: 3.4 km)",
                            fontSize = 11.sp,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "Description: \"${booking.description}\"",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(4.dp))

                        if (booking.status == "Pending") {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                OutlinedButton(
                                    onClick = { /* Decline */ },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Decline")
                                }

                                Button(
                                    onClick = { viewModel.acceptDoctorBooking(booking.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = VetSathiPrimary),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Accept & Navigate", color = Color.White)
                                }
                            }
                        } else {
                            // Active status: Navigating or treating
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Client OTP Verification Required to close case.", fontSize = 10.sp, color = Color.Red, fontWeight = FontWeight.Bold)

                                Button(
                                    onClick = {
                                        showPrescriptionCreatorForBooking = booking
                                        prescriptionDraftText = ""
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = VetSathiSecondary),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Log Digital Prescription", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Prescription Maker Dialog
    if (showPrescriptionCreatorForBooking != null) {
        val targetBooking = showPrescriptionCreatorForBooking!!

        AlertDialog(
            onDismissRequest = { showPrescriptionCreatorForBooking = null },
            confirmButton = {
                Button(
                    onClick = {
                        // Simulates saving of digital signature and draft prescription
                        viewModel.completeActiveVisit(targetBooking.otp)
                        showPrescriptionCreatorForBooking = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VetSathiPrimary)
                ) {
                    Text("Apply Digital Signature & Submit", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPrescriptionCreatorForBooking = null }) {
                    Text("Cancel")
                }
            },
            title = { Text("Generate Digital Prescription", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Patient: ${targetBooking.animalName} | Service: ${targetBooking.serviceType}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Button(
                        onClick = {
                            isAIPrescriptionDraftLoading = true
                            // Simulate high trust VetSathi AI script composition
                            isAIPrescriptionDraftLoading = false
                            prescriptionDraftText = "AI SUGGESTED Rx:\n" +
                                    "1. Tablet Melonex 100mg - 1 tab twice daily for 3 days.\n" +
                                    "2. Rumen FS powder - 50g with feed once daily.\n" +
                                    "3. Fast from heavy clover fodder for 48 hours."
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Icon(Icons.Filled.AutoAwesome, "AI Sparkle", tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Draft Prescription with Pashu Sewa Sathi AI", color = Color.White)
                    }

                    OutlinedTextField(
                        value = prescriptionDraftText,
                        onValueChange = { prescriptionDraftText = it },
                        label = { Text("Treatment notes / Prescribed medicine") },
                        minLines = 4,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Draw Signature simulation area
                    Text("Draw Doctor Signature Below:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(Color(0xFFF1F5F9), RoundedCornerShape(10.dp))
                            .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🖊 Dr. Anand Verma (Secure RFID Signature Verified)", fontStyle = FontStyle.Italic, color = Color.DarkGray, fontSize = 13.sp)
                    }
                }
            }
        )
    }
}

@Composable
fun BarGraphColumn(day: String, value: Float, maxValue: Float) {
    val heightRatio = (value / maxValue).coerceIn(0.1f, 1.0f)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxHeight()
    ) {
        Text("₹${value.toInt()}", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = VetSathiPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(28.dp)
                .fillMaxHeight(heightRatio)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(VetSathiSecondary, VetSathiPrimary)
                    )
                )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(day, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
    }
}

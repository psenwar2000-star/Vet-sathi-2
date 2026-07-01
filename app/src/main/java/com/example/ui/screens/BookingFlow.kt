package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AnimalEntity
import com.example.ui.theme.VetSathiPrimary
import com.example.ui.theme.VetSathiSecondary
import com.example.ui.theme.VetSathiTertiary
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.VetSathiViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingFlowScreen(viewModel: VetSathiViewModel) {
    val selectedAnimal by viewModel.selectedAnimal.collectAsState()
    val animalsList by viewModel.animals.collectAsState()
    val serviceType by viewModel.selectedServiceType.collectAsState()
    val activeBooking by viewModel.activeBooking.collectAsState()

    val doctorLatitude by viewModel.doctorLatitude.collectAsState()
    val doctorLongitude by viewModel.doctorLongitude.collectAsState()
    val doctorEtaMinutes by viewModel.doctorEtaMinutes.collectAsState()

    var bookingDescription by remember { mutableStateOf("") }
    var isRecordingVoice by remember { mutableStateOf(false) }
    var voiceNoteDuration by remember { mutableStateOf(0) }
    var isImageAttached by remember { mutableStateOf(false) }
    var isVideoAttached by remember { mutableStateOf(false) }

    var otpInputText by remember { mutableStateOf("") }
    var showChatWindow by remember { mutableStateOf(false) }

    // Chat messages simulation
    val chatMessages = remember {
        mutableStateListOf(
            "Hello, I am on my way to your farm location in Jodhpur. Please keep the animal isolated in a quiet dry place.",
            "I have packed the vaccine ampoules and sterile needles."
        )
    }
    var chatInputText by remember { mutableStateOf("") }

    // Voice recording timer simulation
    LaunchedEffect(isRecordingVoice) {
        if (isRecordingVoice) {
            voiceNoteDuration = 0
            while (isRecordingVoice) {
                delay(1000)
                voiceNoteDuration++
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Veterinary Care", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(Screen.OWNER_HOME) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAF9))
                .padding(innerPadding)
        ) {
            if (activeBooking == null) {
                // Step 1: Create Case Booking View
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "1. SELECT APPLICABLE ANIMAL",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(animalsList) { animal ->
                            val isSelected = selectedAnimal?.id == animal.id
                            Card(
                                modifier = Modifier
                                    .width(160.dp)
                                    .clickable { viewModel.selectAnimal(animal) },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) VetSathiPrimary.copy(alpha = 0.05f) else Color.White
                                ),
                                border = BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) VetSathiPrimary else Color(0xFFE2E8F0)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(if (animal.species == "Cow") "🐄" else "🐕", fontSize = 24.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(animal.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black)
                                        Text(animal.breed, fontSize = 9.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "Selected Service: $serviceType",
                                fontWeight = FontWeight.Bold,
                                color = VetSathiPrimary,
                                fontSize = 16.sp
                            )

                            OutlinedTextField(
                                value = bookingDescription,
                                onValueChange = { bookingDescription = it },
                                label = { Text("Describe Symptoms / Requirements") },
                                placeholder = { Text("e.g. Cow running high fever, or need Gir cow artificial insemination.") },
                                minLines = 3,
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Text(
                                text = "2. ATTACH MEDIA DIAGNOSTICS (OPTIONAL)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color.Gray,
                                letterSpacing = 1.sp
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Add Photo simulation
                                OutlinedButton(
                                    onClick = { isImageAttached = !isImageAttached },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (isImageAttached) Color(0xFFECFDF5) else Color.Transparent
                                    )
                                ) {
                                    Icon(
                                        imageVector = if (isImageAttached) Icons.Filled.CheckCircle else Icons.Filled.PhotoCamera,
                                        contentDescription = "Camera",
                                        tint = if (isImageAttached) Color(0xFF059669) else VetSathiPrimary
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isImageAttached) "Photo Attached" else "Add Photo",
                                        fontSize = 11.sp,
                                        color = if (isImageAttached) Color(0xFF059669) else Color.Black
                                    )
                                }

                                // Add Video simulation
                                OutlinedButton(
                                    onClick = { isVideoAttached = !isVideoAttached },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (isVideoAttached) Color(0xFFECFDF5) else Color.Transparent
                                    )
                                ) {
                                    Icon(
                                        imageVector = if (isVideoAttached) Icons.Filled.CheckCircle else Icons.Filled.Videocam,
                                        contentDescription = "Video",
                                        tint = if (isVideoAttached) Color(0xFF059669) else VetSathiPrimary
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isVideoAttached) "Video Attached" else "Add Video",
                                        fontSize = 11.sp,
                                        color = if (isVideoAttached) Color(0xFF059669) else Color.Black
                                    )
                                }
                            }

                            // Voice Recording Simulation Row
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isRecordingVoice) Color(0xFFFEF2F2) else Color(0xFFF8FAF9),
                                        RoundedCornerShape(14.dp)
                                    )
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                                    .clickable { isRecordingVoice = !isRecordingVoice }
                                    .padding(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(if (isRecordingVoice) Color.Red else VetSathiPrimary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isRecordingVoice) Icons.Filled.MicOff else Icons.Filled.Mic,
                                            contentDescription = "Mic",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (isRecordingVoice) "Recording Animal Sound..." else "Record Animal Cough/Voice",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isRecordingVoice) Color.Red else Color.Black
                                        )
                                        Text(
                                            text = if (isRecordingVoice) "Timer: $voiceNoteDuration seconds (Click to stop)" else "Helps our vet diagnose throat infections and bloat",
                                            fontSize = 10.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    if (isRecordingVoice) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(Color.Red)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Farmer GPS location confirmation
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.LocationOn, "Location Indicator", tint = VetSathiSecondary, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Farm Location for Dispatch", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("Jodhpur, Rajasthan, India (GPS Auto-detected)", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.createBooking(
                                description = bookingDescription.ifEmpty { "General diagnostic checkup requested." },
                                voiceNotePath = if (voiceNoteDuration > 0) "mock_voice_path.aac" else null,
                                imagePath = if (isImageAttached) "mock_symptom.jpg" else null
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VetSathiPrimary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("request_doctor_button")
                    ) {
                        Text("Confirm & Search Live Doctors", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            } else {
                // Step 2: Live Doctor Tracking & Map Radar View
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(VetSathiPrimary.copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Filled.Person, "Vet Profile Pic", tint = VetSathiPrimary)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = activeBooking?.doctorName ?: "Assigning Doctor...",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = "B.V.Sc & A.H | Verified Expert",
                                            fontSize = 10.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    IconButton(
                                        onClick = { showChatWindow = true },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color(0xFFE0F2FE), CircleShape)
                                    ) {
                                        Icon(Icons.Filled.Chat, "Chat", tint = Color(0xFF0369A1), modifier = Modifier.size(16.dp))
                                    }
                                    IconButton(
                                        onClick = { /* Simulated Call */ },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color(0xFFDCFCE7), CircleShape)
                                    ) {
                                        Icon(Icons.Filled.Call, "Call", tint = Color(0xFF15803D), modifier = Modifier.size(16.dp))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(10.dp))

                            // Interactive Simulated Map Radar & Location Tracker
                            Text(
                                text = "REAL-TIME VET RADAR MAP (JODHPUR, RAJASTHAN)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                letterSpacing = 1.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color(0xFFE2EADD))
                                    .border(1.dp, Color(0xFFC5CDBC), RoundedCornerShape(18.dp))
                            ) {
                                // Draw mock grid and markers on canvas
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    // Grid lines
                                    val gridColor = Color(0xFFB1B9A8)
                                    val lineCount = 10
                                    for (i in 0..lineCount) {
                                        val x = size.width * i / lineCount
                                        drawLine(gridColor, start = androidx.compose.ui.geometry.Offset(x, 0f), end = androidx.compose.ui.geometry.Offset(x, size.height), strokeWidth = 1f)
                                        val y = size.height * i / lineCount
                                        drawLine(gridColor, start = androidx.compose.ui.geometry.Offset(0f, y), end = androidx.compose.ui.geometry.Offset(size.width, y), strokeWidth = 1f)
                                    }
                                }

                                // Farmer marker (Center)
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(14.dp)
                                        .background(Color.Red, CircleShape)
                                        .border(2.dp, Color.White, CircleShape)
                                )

                                // Doctor marker moving live towards center
                                val doctorAlignment = when {
                                    doctorEtaMinutes > 10 -> Alignment.TopEnd
                                    doctorEtaMinutes > 5 -> Alignment.TopStart
                                    doctorEtaMinutes > 2 -> Alignment.BottomStart
                                    else -> Alignment.Center
                                }

                                Box(
                                    modifier = Modifier
                                        .align(doctorAlignment)
                                        .offset(x = (-10).dp, y = 10.dp)
                                        .size(32.dp)
                                        .background(Color(0xFF006B5E), CircleShape)
                                        .border(2.dp, Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Filled.DirectionsCar, "Doctor car", tint = Color.White, modifier = Modifier.size(16.dp))
                                }

                                // Map labels
                                Card(
                                    shape = RoundedCornerShape(8.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.8f)),
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "Vet GPS: ${"%.4f".format(doctorLatitude)}, ${"%.4f".format(doctorLongitude)}",
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        modifier = Modifier.padding(6.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("ESTIMATED ARRIVAL TIME", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = if (doctorEtaMinutes > 0) "$doctorEtaMinutes Minutes" else "Doctor Arrived at Farm ✔",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (doctorEtaMinutes > 0) VetSathiPrimary else Color(0xFF0F9D58)
                                    )
                                }

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "OTP: ${activeBooking?.otp}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color(0xFFD97706),
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    // OTP Verification Box to Complete Visit
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "VERIFY VET TREATMENT COMPLETION",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Provide the secure 4-digit code to the visiting doctor ONLY after the animal has been successfully examined/treated.",
                                fontSize = 11.sp,
                                color = Color.DarkGray
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = otpInputText,
                                    onValueChange = { otpInputText = it },
                                    placeholder = { Text("Enter OTP") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                )

                                Button(
                                    onClick = {
                                        viewModel.completeActiveVisit(otpInputText)
                                    },
                                    enabled = otpInputText.isNotEmpty(),
                                    colors = ButtonDefaults.buttonColors(containerColor = VetSathiSecondary),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.height(52.dp)
                                ) {
                                    Text("Verify & Pay Vet", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    if (activeBooking?.status == "Completed") {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Treatment Successfully Completed ✔", fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                                Text("Payment has been transferred from your VetSathi Wallet safely.", fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { viewModel.navigateTo(Screen.OWNER_HOME) },
                                    colors = ButtonDefaults.buttonColors(containerColor = VetSathiPrimary),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Return to Dashboard", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // In-App Chat Modal
    if (showChatWindow) {
        AlertDialog(
            onDismissRequest = { showChatWindow = false },
            confirmButton = {
                TextButton(onClick = { showChatWindow = false }) { Text("Close") }
            },
            title = { Text("Chat with ${activeBooking?.doctorName ?: "Doctor"}", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        chatMessages.forEach { msg ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFFF1F5F9))
                                        .padding(10.dp)
                                        .fillMaxWidth(0.85f)
                                ) {
                                    Text(msg, fontSize = 12.sp, color = Color.Black)
                                }
                            }
                        }
                    }

                    Divider()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = chatInputText,
                            onValueChange = { chatInputText = it },
                            placeholder = { Text("Type massage...", fontSize = 12.sp) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                if (chatInputText.isNotEmpty()) {
                                    chatMessages.add(chatInputText)
                                    chatInputText = ""
                                }
                            },
                            modifier = Modifier.background(VetSathiPrimary, CircleShape)
                        ) {
                            Icon(Icons.Filled.Send, "Send", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        )
    }
}

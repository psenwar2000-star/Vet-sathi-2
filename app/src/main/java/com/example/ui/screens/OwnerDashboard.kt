package com.example.ui.screens

import android.graphics.Bitmap
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AnimalEntity
import com.example.data.model.BookingEntity
import com.example.ui.theme.VetSathiPrimary
import com.example.ui.theme.VetSathiSecondary
import com.example.ui.theme.VetSathiTertiary
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.UiState
import com.example.ui.viewmodel.VetSathiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDashboardScreen(viewModel: VetSathiViewModel) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Home, 1: Bookings, 2: Animals, 3: Health Records, 4: AI & Schemes

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.height(80.dp)
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Filled.Home, "Home", modifier = Modifier.size(24.dp)) },
                    label = { Text("Home", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = VetSathiPrimary,
                        selectedTextColor = VetSathiPrimary,
                        indicatorColor = Color(0xFFD0E7E3)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Filled.DateRange, "Bookings", modifier = Modifier.size(24.dp)) },
                    label = { Text("Bookings", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = VetSathiPrimary,
                        selectedTextColor = VetSathiPrimary,
                        indicatorColor = Color(0xFFD0E7E3)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Filled.Pets, "Animals", modifier = Modifier.size(24.dp)) },
                    label = { Text("Animals", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = VetSathiPrimary,
                        selectedTextColor = VetSathiPrimary,
                        indicatorColor = Color(0xFFD0E7E3)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Filled.Assignment, "Records", modifier = Modifier.size(24.dp)) },
                    label = { Text("Records", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = VetSathiPrimary,
                        selectedTextColor = VetSathiPrimary,
                        indicatorColor = Color(0xFFD0E7E3)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Filled.AutoAwesome, "VetAI", modifier = Modifier.size(24.dp)) },
                    label = { Text("VetAI", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = VetSathiPrimary,
                        selectedTextColor = VetSathiPrimary,
                        indicatorColor = Color(0xFFD0E7E3)
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAF9))
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> HomeTab(viewModel)
                1 -> BookingsTab(viewModel)
                2 -> AnimalsTab(viewModel)
                3 -> HealthRecordsTab(viewModel)
                4 -> VetAiTab(viewModel)
            }
        }
    }
}

@Composable
fun HomeTab(viewModel: VetSathiViewModel) {
    val selectedAnimal by viewModel.selectedAnimal.collectAsState()
    val activeBooking by viewModel.activeBooking.collectAsState()
    val walletBalance by viewModel.walletBalance.collectAsState()
    val animalsList by viewModel.animals.collectAsState()

    val doctorLatitude by viewModel.doctorLatitude.collectAsState()
    val doctorLongitude by viewModel.doctorLongitude.collectAsState()
    val doctorEtaMinutes by viewModel.doctorEtaMinutes.collectAsState()

    var showAddAnimalDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // App Header conforming to Sleek Interface
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "LIVE FARMER GPS LOCATION",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    letterSpacing = 1.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { /* Select location */ }
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF006B5E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Jodhpur, Rajasthan",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Change Location",
                        tint = Color.Gray
                    )
                }
            }

            // Quick Actions: Wallet & Power Off
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Wallet chip
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    onClick = { viewModel.addFundsToWallet(500.0) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🪙", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "₹${walletBalance.toInt()}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F9D58)
                        )
                    }
                }

                // Profile / Role picker icon
                IconButton(
                    onClick = { viewModel.logOut() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = "Switch Role",
                        tint = Color.Red,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Active Booking Notification Banner (Real-time Uber Tracking Status)
        AnimatedVisibility(
            visible = activeBooking != null && activeBooking?.status != "Completed" && activeBooking?.status != "Cancelled",
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFECEB)),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.2.dp, Color(0xFFFFDAD6))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.DirectionsCar,
                                contentDescription = "Active Travel",
                                tint = Color(0xFFBA1A1A)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Active Case: ${activeBooking?.serviceType}",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF410002)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFBA1A1A))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = activeBooking?.status?.uppercase() ?: "PENDING",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${activeBooking?.doctorName} is dispatched to your farm location.",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.navigateTo(Screen.BOOKING_FLOW) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA1A1A)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Open Live Radar & Map Tracker", color = Color.White)
                    }
                }
            }
        }

        // Quick Search Section
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Search doctors, AI disease logs, prescriptions...", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search icon") },
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.navigateTo(Screen.AI_ASSISTANT) },
                enabled = false
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Large Premium Emergency SOS Red Block
        Box(modifier = Modifier.padding(horizontal = 20.dp)) {
            Button(
                onClick = {
                    viewModel.setServiceType("Emergency SOS")
                    viewModel.createBooking("URGENT: Animal needs rescue, high fever or delivery complications!")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA1A1A)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("sos_emergency_btn")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "Warning",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "EMERGENCY VETERINARY SOS",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // AI Vet Scanner / Detection Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF0F9D58), Color(0xFF13B5B1))
                    )
                )
                .clickable { viewModel.navigateTo(Screen.AI_ASSISTANT) }
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = "AI Live",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "VETAI LIVE SCANNER",
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Symptom AI Analyzer",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "Upload symptoms or take physical pictures of lesions/lumps to identify 20+ bovine & pet conditions.",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { viewModel.navigateTo(Screen.AI_ASSISTANT) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Scan Symptoms Now", color = Color(0xFF0D6B40), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            // Sparkle graphics in the background
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 10.dp, y = 10.dp)
            ) {
                Text("🧬", fontSize = 90.sp, modifier = Modifier.alpha(0.2f))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Services Grid Section
        Text(
            text = "INSTANT VETERINARY SERVICES",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ServiceGridItem(
                emoji = "🏠",
                title = "Home Visit",
                onClick = {
                    viewModel.setServiceType("Doctor Home Visit")
                    viewModel.navigateTo(Screen.BOOKING_FLOW)
                }
            )
            ServiceGridItem(
                emoji = "📹",
                title = "Video Consult",
                onClick = {
                    viewModel.setServiceType("Video Consultation")
                    viewModel.navigateTo(Screen.BOOKING_FLOW)
                }
            )
            ServiceGridItem(
                emoji = "🌱",
                title = "AI Breeding",
                onClick = {
                    viewModel.setServiceType("Artificial Insemination")
                    viewModel.navigateTo(Screen.BOOKING_FLOW)
                }
            )
            ServiceGridItem(
                emoji = "🐄",
                title = "Pregnancy Dx",
                onClick = {
                    viewModel.setServiceType("Pregnancy Diagnosis")
                    viewModel.navigateTo(Screen.BOOKING_FLOW)
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ServiceGridItem(
                emoji = "💉",
                title = "Vaccines",
                onClick = {
                    viewModel.setServiceType("Herd Vaccination")
                    viewModel.navigateTo(Screen.BOOKING_FLOW)
                }
            )
            ServiceGridItem(
                emoji = "💊",
                title = "Order Medicines",
                onClick = {
                    viewModel.setServiceType("Medicine Delivery")
                    viewModel.navigateTo(Screen.BOOKING_FLOW)
                }
            )
            ServiceGridItem(
                emoji = "🧪",
                title = "Lab Blood Test",
                onClick = {
                    viewModel.setServiceType("Pathology Lab Test")
                    viewModel.navigateTo(Screen.BOOKING_FLOW)
                }
            )
            ServiceGridItem(
                emoji = "🛡️",
                title = "Animal Insurance",
                onClick = {
                    viewModel.setServiceType("Livestock Insurance Audit")
                    viewModel.navigateTo(Screen.BOOKING_FLOW)
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Live Radar Map Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "LIVE AREA VET RADAR",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Real-time trackable doctor locations and status",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE6F4EA))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "LIVE GPS",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF006B5E)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LiveVetMapComponent(
                activeBooking = activeBooking,
                doctorLatitude = doctorLatitude,
                doctorLongitude = doctorLongitude,
                doctorEtaMinutes = doctorEtaMinutes,
                animals = animalsList,
                onSelectDoctor = { doctorName ->
                    viewModel.setServiceType("Doctor Home Visit")
                    viewModel.navigateTo(Screen.BOOKING_FLOW)
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Animal Health Passport Section (Active selection)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "YOUR ANIMAL PASSPORTS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                letterSpacing = 1.sp
            )
            Text(
                text = "+ Add Animal",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = VetSathiPrimary,
                modifier = Modifier.clickable { showAddAnimalDialog = true }
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(animalsList) { animal ->
                val isSelected = selectedAnimal?.id == animal.id
                Card(
                    modifier = Modifier
                        .width(280.dp)
                        .clickable { viewModel.selectAnimal(animal) },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color.White else Color(0xFFF1F5F9)
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) VetSathiPrimary else Color(0xFFE2E8F0)
                    )
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
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(VetSathiPrimary.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (animal.species.lowercase() == "cow") "🐄" else "🐕",
                                        fontSize = 24.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = animal.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color(0xFF1E293B)
                                    )
                                    Text(
                                        text = "${animal.breed} | ${animal.gender}",
                                        fontSize = 10.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFD1FAE5))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "PASS: VERIFIED",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF065F46)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Divider(color = Color(0xFFE2E8F0))

                        Spacer(modifier = Modifier.height(8.dp))

                        // Stats & Info Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("TAG NUMBER", fontSize = 8.sp, color = Color.Gray)
                                Text(animal.tagNumber, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                            }
                            Column {
                                Text("MILK YIELD", fontSize = 8.sp, color = Color.Gray)
                                Text(animal.milkProduction, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                            }
                            Column {
                                Text("PREGNANCY", fontSize = 8.sp, color = Color.Gray)
                                Text(animal.pregnancyStatus, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Verified, "Shield", tint = VetSathiSecondary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Next immunization due: ${animal.vaccinationHistory}",
                                fontSize = 9.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    // Add Animal Dialog
    if (showAddAnimalDialog) {
        var name by remember { mutableStateOf("") }
        var species by remember { mutableStateOf("Cow") }
        var breed by remember { mutableStateOf("") }
        var age by remember { mutableStateOf("") }
        var weight by remember { mutableStateOf("") }
        var tagNumber by remember { mutableStateOf("") }
        var milkProduction by remember { mutableStateOf("0L/day") }

        AlertDialog(
            onDismissRequest = { showAddAnimalDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotEmpty() && breed.isNotEmpty()) {
                            viewModel.addAnimal(
                                name = name,
                                species = species,
                                breed = breed,
                                age = age,
                                weight = weight,
                                gender = "Female",
                                tagNumber = tagNumber.ifEmpty { "IN-MOCK-9921" },
                                milk = milkProduction,
                                pregnancy = "Not Pregnant"
                            )
                            showAddAnimalDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VetSathiPrimary)
                ) {
                    Text("Register Animal", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddAnimalDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Register Animal Digital Passport", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Animal Name (e.g. Gauri)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Species simple drop selectors
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Cow", "Dog", "Buffalo", "Goat").forEach { sp ->
                            val selected = species == sp
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selected) VetSathiPrimary else Color(0xFFF1F5F9))
                                    .clickable { species = sp }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = sp,
                                    fontSize = 11.sp,
                                    color = if (selected) Color.White else Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = breed,
                        onValueChange = { breed = it },
                        label = { Text("Breed (e.g. Sahiwal, Holstein)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = age,
                            onValueChange = { age = it },
                            label = { Text("Age") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = weight,
                            onValueChange = { weight = it },
                            label = { Text("Weight (kg)") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = tagNumber,
                        onValueChange = { tagNumber = it },
                        label = { Text("Govt Tag Number (if any)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (species == "Cow" || species == "Buffalo") {
                        OutlinedTextField(
                            value = milkProduction,
                            onValueChange = { milkProduction = it },
                            label = { Text("Current Daily Milk Yield") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun ServiceGridItem(
    emoji: String,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(78.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                textAlign = TextAlign.Center,
                lineHeight = 11.sp,
                maxLines = 2
            )
        }
    }
}

@Composable
fun BookingsTab(viewModel: VetSathiViewModel) {
    val bookingsList by viewModel.bookings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Booking history",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
        Text(
            text = "Track real-time cases, invoices, & certificates",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (bookingsList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No cases requested yet.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                items(bookingsList) { booking ->
                    BookingHistoryCard(booking, viewModel)
                }
            }
        }
    }
}

@Composable
fun BookingHistoryCard(booking: BookingEntity, viewModel: VetSathiViewModel) {
    var showInvoiceDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = booking.serviceType,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = "Patient: ${booking.animalName} | ${booking.date}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when (booking.status.lowercase()) {
                                "completed" -> Color(0xFFD1FAE5)
                                "active" -> Color(0xFFFFECEB)
                                else -> Color(0xFFFEF3C7)
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = booking.status.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (booking.status.lowercase()) {
                            "completed" -> Color(0xFF065F46)
                            "active" -> Color(0xFFBA1A1A)
                            else -> Color(0xFFD97706)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Assigned Vet: ${booking.doctorName}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )

            if (booking.description.isNotEmpty()) {
                Text(
                    text = "Notes: \"${booking.description}\"",
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Amount Paid: ₹${booking.invoiceAmount.toInt()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { showInvoiceDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("Invoice PDF", fontSize = 10.sp)
                    }

                    if (booking.status == "Completed" && booking.rating == 0f) {
                        Button(
                            onClick = { viewModel.addRatingToBooking(booking, 5.0f) },
                            colors = ButtonDefaults.buttonColors(containerColor = VetSathiSecondary),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text("Rate 5 ⭐", fontSize = 10.sp)
                        }
                    } else if (booking.rating > 0) {
                        Text("⭐ ${booking.rating}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFFD97706))
                    }
                }
            }
        }
    }

    if (showInvoiceDialog) {
        AlertDialog(
            onDismissRequest = { showInvoiceDialog = false },
            confirmButton = {
                Button(onClick = { showInvoiceDialog = false }) { Text("OK") }
            },
            title = { Text("Official Pashu Sewa Sathi Invoice", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Invoice No: PSS-2026-${booking.id}")
                    Text("Date: ${booking.date}")
                    Text("Client Location: ${booking.location}")
                    Text("Doctor: ${booking.doctorName}")
                    Text("Patient Passport ID: PASS-${booking.animalId}")
                    Divider()
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Service Fee:")
                        Text("₹${booking.invoiceAmount}")
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Tax & GST (5%):")
                        Text("₹${(booking.invoiceAmount * 0.05)}")
                    }
                    Divider()
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Grand Total:", fontWeight = FontWeight.Bold)
                        Text("₹${(booking.invoiceAmount * 1.05)}", fontWeight = FontWeight.Bold, color = VetSathiPrimary)
                    }
                    Text(
                        text = "Status: PAID via ${booking.paymentMethod}",
                        color = Color(0xFF0F9D58),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        )
    }
}

@Composable
fun AnimalsTab(viewModel: VetSathiViewModel) {
    val animalsList by viewModel.animals.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Livestock & Pet Profiles",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
        Text(
            text = "Active RFID tag numbers & milk telemetry log",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(animalsList) { animal ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(VetSathiPrimary.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(if (animal.species == "Cow") "🐄" else "🐕", fontSize = 28.sp)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(animal.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
                                Text("Species: ${animal.species} | Breed: ${animal.breed}", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text("AGE", fontSize = 8.sp, color = Color.Gray)
                                Text(animal.age, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("WEIGHT", fontSize = 8.sp, color = Color.Gray)
                                Text(animal.weight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("RFID TAG", fontSize = 8.sp, color = Color.Gray)
                                Text(animal.tagNumber, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VetSathiPrimary)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Vaccination Cover: ${animal.vaccinationHistory}",
                            fontSize = 10.sp,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "Reproductive Log: ${animal.pregnancyStatus}",
                            fontSize = 10.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HealthRecordsTab(viewModel: VetSathiViewModel) {
    val records by viewModel.healthRecords.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Medical Timelines",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
        Text(
            text = "Legally binding digital veterinary prescriptions",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (records.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No medical records logged yet.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(records) { record ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = record.animalName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = VetSathiPrimary
                                )
                                Text(
                                    text = record.date,
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Diagnosis: ${record.diagnosis}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "Action Taken: ${record.treatment}",
                                fontSize = 12.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF8FAF9), RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "DIGITAL PRESCRIPTION BY ${record.vetName.uppercase()}",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = record.prescription,
                                        fontSize = 11.sp,
                                        fontStyle = FontStyle.Italic,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VetAiTab(viewModel: VetSathiViewModel) {
    val schemes by viewModel.governmentSchemes.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Pashu Sewa Sathi AI & Resources",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
        Text(
            text = "Government schemes, subsidy matching & disease diagnostic portals",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = { viewModel.navigateTo(Screen.AI_ASSISTANT) },
            colors = ButtonDefaults.buttonColors(containerColor = VetSathiPrimary),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Icon(Icons.Filled.AutoAwesome, "AI Icon")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Open Diagnostic AI Assistant", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "LIVE GOVERNMENT SCHEMES & SUBSIDIES",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        schemes.forEach { scheme ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEF3C7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🇮🇳", fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = scheme.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF1E293B)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = scheme.description,
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFECFDF5), RoundedCornerShape(10.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "BENEFIT: ${scheme.benefit}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color(0xFF065F46)
                        )
                    }
                }
            }
        }
    }
}
